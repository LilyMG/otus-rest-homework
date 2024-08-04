package otus.services;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provides;
import otus.dto.pet.PetDTO;
import otus.specs.AbsBaseSpecs;

public class PetRestClient extends AbsBaseSpecs {

    @Inject
    public PetRestClient() {
        requestSpecification.basePath("/pet");
    }

    public PetDTO create(PetDTO petDTO) {
        Gson gson = new Gson();
        int id = requestSpecification.
                when().
                header("accept", "application/json").
                header("Content-Type", "application/json").
                body(gson.toJson(petDTO)).
                post().
                then().
                statusCode(200).
                log().all().extract().path("id");
        petDTO.setId(id);
        return petDTO;
    }

    public void update(PetDTO petDTO, int expectedStatus) {
        Gson gson = new Gson();
        requestSpecification.
                when().
                header("accept", "application/json").
                header("Content-Type", "application/json").
                body(gson.toJson(petDTO)).
                log().all().
                put("/").
                then().
                statusCode(expectedStatus).
                log().all();
    }

    public PetDTO get(long id) {
        return requestSpecification.
                when().
                log().all().
                get("/" + id).
                then().
                statusCode(200).
                log().all().
                extract().body().as(PetDTO.class);
    }

    public PetDTO delete(long id) {
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
