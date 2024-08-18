package otus.extensions;

import com.github.javafaker.Faker;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import otus.dto.pet.PetDTO;
import otus.dto.pet.Tag;
import otus.modules.GuiceModule;
import otus.services.PetRestClient;

import java.util.ArrayList;
import java.util.List;

public class ApiExtensions implements AfterEachCallback, BeforeEachCallback {
    private final Injector injector = Guice.createInjector(new GuiceModule());
    @Inject
    private final PetRestClient petRestClient = injector.getInstance(PetRestClient.class);
    private final Faker faker = new Faker();


    @Override
    public void beforeEach(ExtensionContext context) {
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
        petDTO = petRestClient.create(petDTO);

        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(PetDTO.class));
        store.put("createdPet", petDTO);
    }


    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(PetDTO.class));
        PetDTO createdPet = store.get("createdPet", PetDTO.class);
        store.remove("createdPet");
        petRestClient.delete(createdPet.getId());
    }

}
