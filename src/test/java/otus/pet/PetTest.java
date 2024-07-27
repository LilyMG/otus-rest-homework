package otus.pet;

import com.github.javafaker.Faker;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import otus.dto.pet.PetDTO;
import otus.dto.pet.Tag;
import otus.extensions.ApiExtensions;
import otus.extensions.ExtensionParameterResolver;
import otus.modules.GuiceModule;
import otus.services.PetRestClient;

import java.util.ArrayList;
import java.util.List;

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
        String value = store.get("petID", String.class);

        Tag tag = Tag.builder().
                name(faker.animal().name()).
                id(faker.number().randomDigit()).
                build();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);

        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(faker.letterify("******"));

        PetDTO petDTO = PetDTO.builder().
                id(faker.number().randomDigit()).
                name("test_" + faker.gameOfThrones().dragon()).
                tags(tags).
                photoUrls(photoUrls).
                status("available").//TODO make a enum
                        build();

        petRestClient.create(petDTO);
        String expectedName = "test_" + faker.gameOfThrones().dragon();
        petDTO.setName(expectedName);
        petRestClient.update(petDTO, 200);
        PetDTO petUpdated = petRestClient.get(petDTO.getId());
        assertThat(petUpdated.getName()).as("name was successfully updated").isEqualTo(expectedName);

    }

}
