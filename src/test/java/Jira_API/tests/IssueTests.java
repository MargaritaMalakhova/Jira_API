package Jira_API.tests;

import Jira_API.tests.helpers.CookieGetter;
import Jira_API.tests.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static Jira_API.tests.specs.Specifications.*;
import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class IssueTests extends TestBase {
    static String cookieValue;
    @BeforeAll
    public static void getCookieValue() {
        CookieGetter cookieGetter = new CookieGetter();
        cookieValue = cookieGetter.getCookie();
    }

    @Test
    public void createIssueTest() {

        String projectKey = "AT";
        String summary = "API Defect";
        String description = "Defect using the Postman";
        String id = "10005";

        Fields fields = new Fields();
        Project project = new Project();
        project.setKey(projectKey);
        fields.setProject(project);
        fields.setSummary(summary);
        fields.setDescription(description);
        IssueType issueType = new IssueType();
        issueType.setId(id);
        fields.setIssuetype(issueType);

        CreationIssueRequestModel creationIssueRequestModel = new CreationIssueRequestModel();
        creationIssueRequestModel.setFields(fields);

        CreationIssueResponseModel response = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .body(creationIssueRequestModel)
                .when()
                .post("/rest/api/2/issue")
                .then()
                .spec(responseSpec201)
                .extract().as(CreationIssueResponseModel.class);

                String idFromResopnse = response.getId();
                String keyFromResponse = response.getKey();

        CreationIssueResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .get("/rest/api/2/issue/" + idFromResopnse)
                .then()
                .spec(responseSpec200)
                .extract().as(CreationIssueResponseModel.class);
                assertEquals(idFromResopnse, getResponse.getId());
                assertEquals(keyFromResponse, getResponse.getKey());
    }
}
