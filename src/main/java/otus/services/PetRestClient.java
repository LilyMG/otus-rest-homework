package otus.services;

import com.google.inject.Inject;
import com.google.inject.Provides;
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

    public void create(PetDTO petDTO) {
        requestSpecification.
                when().
                log().all().
                post("/").
                then().
                statusCode(200).
                log().all();
    }

    public PetDTO get(int id) {
        return requestSpecification.
                when().
                log().all().
                get("/" + id).
                then().
                statusCode(200).
                log().all().
                extract().body().as(PetDTO.class);
    }

}
