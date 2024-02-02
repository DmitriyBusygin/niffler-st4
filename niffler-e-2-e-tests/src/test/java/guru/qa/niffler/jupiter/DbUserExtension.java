package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;

public class DbUserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbUserExtension.class);
    private static final UserRepository userRepository = new UserRepositoryJdbc();
    private static final Faker faker = new Faker();
    private static final String userAuthEntityKey = "userAuthEntity";
    private static final String userEntityKey = "userEntity";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // список методов вызываемые в рамках теста
        List<Method> methodsCalledInTest = new ArrayList<>();
        // метод теста
        methodsCalledInTest.add(context.getRequiredTestMethod());
        // список методов BeforeEach
        Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .forEach(methodsCalledInTest::add);
        // получить вызываемые методы с аннотацией DbUser в рамках теста.
        List<Method> methodsWithDbUserCalledInTest = methodsCalledInTest.stream()
                .filter(method -> method.isAnnotationPresent(DbUser.class))
                .toList();
        // получить первую аннотацию с DbUser
        DbUser userFromAnnotation = methodsWithDbUserCalledInTest.get(0).getAnnotation(DbUser.class);

        // логика создания пользователя
        Map<String, Object> createdUsers = new HashMap<>();

        String userName = userFromAnnotation.userName().isEmpty() ? faker.name().firstName() : userFromAnnotation.userName();
        String userPassword = userFromAnnotation.password().isEmpty() ? faker.internet().password() : userFromAnnotation.password();

        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername(userName);
        userAuthEntity.setPassword(userPassword);
        userAuthEntity.setEnabled(true);
        userAuthEntity.setAccountNonExpired(true);
        userAuthEntity.setAccountNonLocked(true);
        userAuthEntity.setCredentialsNonExpired(true);
        userAuthEntity.setAuthorities(Arrays.stream(Authority.values())
                .map(e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(e);
                    return ae;
                }).toList()
        );

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userName);
        userEntity.setCurrency(CurrencyValues.RUB);

        userRepository.createInAuth(userAuthEntity);
        userRepository.createInUserdata(userEntity);

        createdUsers.put(userAuthEntityKey, userAuthEntity);
        createdUsers.put(userEntityKey, userEntity);

        context.getStore(NAMESPACE).put(context.getUniqueId(), createdUsers);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (UserAuthEntity) extensionContext
                .getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(userAuthEntityKey);
    }


    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map<String, Object> createdUsers = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        UserAuthEntity userAuthEntity = (UserAuthEntity) createdUsers.get(userAuthEntityKey);
        UserEntity userEntity = (UserEntity) createdUsers.get(userEntityKey);

        userRepository.deleteInAuthById(userAuthEntity.getId());
        userRepository.deleteInUserdataById(userEntity.getId());
    }
}
