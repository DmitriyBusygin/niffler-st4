package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public abstract class SpendExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);

    public abstract SpendJson create(SpendJson spend) throws IOException;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GenerateSpend.class
        );

        if (spend.isPresent()) {
            GenerateSpend spendData = spend.get();
            SpendJson spendJson = new SpendJson(
                    null,
                    spendData.username(),
                    new Date(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.category()
            );

            SpendJson createdSpend = create(spendJson);

            context.getStore(NAMESPACE).put(context.getUniqueId(), createdSpend);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(SpendExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
