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
public class PetTest {
    private final Faker faker = new Faker();
    private final Injector injector = Guice.createInjector(new GuiceModule());
    @Inject
    private final PetRestClient petRestClient = injector.getInstance(PetRestClient.class);

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

}
