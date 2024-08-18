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

@ExtendWith(ApiExtensions.class)
@ExtendWith(ExtensionParameterResolver.class)
public class GetPetTest {

    private final Injector injector = Guice.createInjector(new GuiceModule());
    @Inject
    private final Faker faker = injector.getInstance(Faker.class);
    @Inject
    private final PetRestClient petRestClient = injector.getInstance(PetRestClient.class);

    /**
     * Check Successful Pet Update
     */
    @Test
    public void updatePetObject(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(PetDTO.class));
        PetDTO createdPet = store.get("createdPet", PetDTO.class);
        String expectedName = "test_" + faker.gameOfThrones().dragon();
        createdPet.setName(expectedName);
        petRestClient.update(createdPet, 200);
        PetDTO petUpdated = petRestClient.get(createdPet.getId());
        assertThat(petUpdated.getName()).as("name wasn't successfully updated").isEqualTo(expectedName);
    }

    /**
     * Check impossibility of updating pet with incorrect data
     */
    @Test
    public void updatePetObjectWithIncorrectData(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(PetDTO.class));
        PetDTO createdPet = store.get("createdPet", PetDTO.class);
        String originalName = createdPet.getName();
        String expectedName = "test_" + faker.harryPotter().spell();
        createdPet.setName(expectedName);
        createdPet.setStatus("someIncorrectStatus");
        petRestClient.update(createdPet, 200);
        PetDTO petUpdated = petRestClient.get(createdPet.getId());
        assertThat(petUpdated.getName()).as("name was updated with incorrect status").isEqualTo(originalName);
    }
}
