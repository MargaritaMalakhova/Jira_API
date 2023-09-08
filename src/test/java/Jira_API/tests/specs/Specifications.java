package Jira_API.tests.specs;

import Jira_API.tests.helpers.CookieGetter;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;


public class Specifications {

    public static RequestSpecification requestSpec = with()
            .log().all()
            .contentType(JSON);

    public static RequestSpecification requestSpecAttach = with()
            .log().all()
            .contentType("multipart/form-data");

    public static ResponseSpecification responseSpec200 = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification responseSpec201 = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification responseSpec204 = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification responseSpec404 = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(404)
            .build();
}

