package otus.specs;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class AbsBaseSpecs {

    private final String BASE_URI = System.getProperty("base.url");
    protected RequestSpecification requestSpecification = given().baseUri(BASE_URI).contentType(ContentType.JSON);
}
