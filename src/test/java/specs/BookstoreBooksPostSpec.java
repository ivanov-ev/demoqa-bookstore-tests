package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.*;
import static io.restassured.http.ContentType.JSON;

public class BookstoreBooksPostSpec {
    public static RequestSpecification BookstoreBooksPostRequestSpec = with()
            .filter(withCustomTemplates())
            .contentType(JSON)
            .log().method()
            .log().uri()
            .log().body()
            .basePath("/BookStore/v1/Books");

    public static ResponseSpecification BookstoreBooksPostResponseSpec = new ResponseSpecBuilder()
            .log(URI)
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(201)
            .build();
}
