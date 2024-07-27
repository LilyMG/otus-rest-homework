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
import otus.modules.GuiceModule;
import otus.services.PetRestClient;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApiExtensions.class)
public class PetTest {
    private final Faker faker = new Faker();
    @Inject
    private PetRestClient petRestClient;

    @Test
    public void updatePetObject(ExtensionContext extensionContext) {

        Injector injector = Guice.createInjector(new GuiceModule());
        petRestClient = injector.getInstance(PetRestClient.class);

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
