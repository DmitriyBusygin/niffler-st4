package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.User.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Map<User.UserType, Queue<UserJson>> USERS_STORAGE = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> friendsQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> commonQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> invitationSendQueue = new ConcurrentLinkedQueue<>();
        Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();

        friendsQueue.add(user("dima", "12345", WITH_FRIENDS, "duck"));
        friendsQueue.add(user("duck", "12345", WITH_FRIENDS, "dima"));
        commonQueue.add(user("bee", "12345", COMMON, null));
        commonQueue.add(user("barsik", "12345", COMMON, null));
        invitationSendQueue.add(user("dog", "12345", INVITATION_SEND, "simpson"));
        invitationSendQueue.add(user("frog", "12345", INVITATION_SEND, "lama"));
        invitationReceivedQueue.add(user("simpson", "12345", INVITATION_RECEIVED, "dog"));
        invitationReceivedQueue.add(user("lama", "12345", INVITATION_RECEIVED, "frog"));

        USERS_STORAGE.put(WITH_FRIENDS, friendsQueue);
        USERS_STORAGE.put(COMMON, commonQueue);
        USERS_STORAGE.put(INVITATION_SEND, invitationSendQueue);
        USERS_STORAGE.put(INVITATION_RECEIVED, invitationReceivedQueue);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        // список методов вызываемые в рамках теста
        List<Method> methodsCalledInTest = new ArrayList<>();
        // метод теста
        methodsCalledInTest.add(context.getRequiredTestMethod());
        // список методов BeforeEach
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(methodsCalledInTest::add);
        // получить параметры с аннотацией @User в рамках методов вызванного теста
        List<Parameter> parameters = methodsCalledInTest.stream()
                .map(Executable::getParameters)
                .flatMap(Arrays::stream)
                .filter(parameter -> parameter.isAnnotationPresent(User.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                .toList();

        Map<User.UserType, UserJson> usersNeededForTest = new HashMap<>();
        for (Parameter parameter : parameters) {
            User.UserType userType = parameter.getAnnotation(User.class).value();
            // если пользователя такого типа уже создали, то берем следующий параметр
            if (usersNeededForTest.containsKey(userType)) {
                continue;
            }

            UserJson userFromQueue = null;
            Queue<UserJson> queueWithRequiredUserType = USERS_STORAGE.get(userType);
            while (userFromQueue == null) {
                userFromQueue = queueWithRequiredUserType.poll();
            }

            usersNeededForTest.put(userType, userFromQueue);
        }

        context.getStore(NAMESPACE).put(context.getUniqueId(), usersNeededForTest);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        for (User.UserType userType : usersFromTest.keySet()) {
            USERS_STORAGE.get(userType).add(usersFromTest.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                       .getType()
                       .isAssignableFrom(UserJson.class) &&
               parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserJson) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(User.class).get().value());

    }

    private static UserJson user(String username, String password, User.UserType userType, String friendsName) {
        return new UserJson(
                null,
                username,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                new TestData(
                        password,
                        userType,
                        friendsName
                )
        );
    }
}
