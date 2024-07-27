package otus.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiExtensions implements AfterEachCallback, BeforeEachCallback {
    private static final String CONTEXT_KEY = "context";

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        extensionContext.publishReportEntry("hola", "holala");
    }


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.GLOBAL);
        store.put(CONTEXT_KEY, context);
    }

}
