package otus.services;

import com.google.inject.Inject;
import otus.dto.pet.PetDTO;
import otus.specs.AbsBaseSpecs;

public class PetRestClient extends AbsBaseSpecs {

    @Inject
    public PetRestClient() {
        requestSpecification.basePath("/pet");
    }

    public void update(PetDTO petDTO, int expectedStatus) {
        requestSpecification.
                when().
                log().all().
                put("/").
                then().
                statusCode(expectedStatus).
                log().all();
    }

}
