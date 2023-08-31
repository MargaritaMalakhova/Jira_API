package Jira_API.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static io.restassured.http.ContentType.JSON;


public class IssueTests {
    @Test
    public void creatIsueTest() {
        RestAssured.baseURI = "http://localhost:8080";
        given().relaxedHTTPSValidation()
                .header("cookie", "JSESSIONID=8D93582153FF052395C6B73786D2F052")
                .log().all()
                .contentType(JSON)
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"AT\"\n" +
                        "       },\n" +
                        "       \"summary\": \"API Defect\",\n" +
                        "       \"description\": \"Defect using the Postman\",\n" +
                        "       \"issuetype\": {\n" +
                        "          \"id\": \"10005\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}")
                .when()
                .post("/rest/api/2/issue")
                .then()
                .log().all()
                .statusCode(201);

    }


}
