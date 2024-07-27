package otus.modules;

// AppModule.java

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import otus.services.PetRestClient;

public class GuiceModule extends AbstractModule {
//    @Override
//    protected void configure() {
//        // Bind other classes or interfaces if needed
//    }

    @Provides
    @Singleton
    public PetRestClient providePetRestClient() {
        return new PetRestClient();
    }
}