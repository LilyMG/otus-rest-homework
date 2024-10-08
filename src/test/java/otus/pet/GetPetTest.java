package otus.pet;

import com.github.javafaker.Faker;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import otus.dto.pet.PetDTO;
import otus.extensions.ApiExtensions;
import otus.extensions.ExtensionParameterResolver;
import otus.modules.GuiceModule;
import otus.services.PetRestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ApiExtensions.class, ExtensionParameterResolver.class})
public class GetPetTest {

    private final Injector injector = Guice.createInjector(new GuiceModule());
    @Inject
    private final PetRestClient petRestClient = injector.getInstance(PetRestClient.class);

    /**
     * Check functionality of getting a pet
     */
    @Test
    public void getPetObject(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(PetDTO.class));
        PetDTO createdPet = store.get("createdPet", PetDTO.class);
        PetDTO petUpdated = petRestClient.get(createdPet.getId(), 200);
        assertThat(createdPet).as("get did not work as expected").isEqualTo(petUpdated);
    }

    /**
     * Check impossibility of getting a pet with incorrect data
     */
    @Test
    public void getPetObjectWithIncorrectData() {
        PetDTO petUpdated = petRestClient.get(1212121212, 404);
        assertThat(petUpdated.getId()).as("there was a pet under non existing pet").isEqualTo(0);
    }
}
