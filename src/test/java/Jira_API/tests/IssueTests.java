package Jira_API.tests;

import Jira_API.tests.helpers.CookieGetter;
import Jira_API.tests.helpers.IssueGetter;
import Jira_API.tests.helpers.IssueRemover;
import Jira_API.tests.models.*;
import org.junit.jupiter.api.*;

import java.io.File;

import static Jira_API.tests.specs.Specifications.*;
import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class IssueTests extends TestBase {
    //TODO: add tags
    private static String cookieValue;
    private static String issueId;
    @BeforeAll
    public static void getCookieValue() {
        CookieGetter cookieGetter = new CookieGetter();
        cookieValue = cookieGetter.getCookie();
    }

    @BeforeEach
    public void createIssue(TestInfo testInfo) {
        if(testInfo.getTags().contains("CreateIssue")) {
            return;
        }
        IssueGetter issueGetter = new IssueGetter();
        issueId = issueGetter.createIssue(cookieValue);
    }

    @AfterEach
    public void removeIssue(TestInfo testInfo) {
            if(testInfo.getTags().contains("RemoveIssue")) {
                return;
            }

        IssueRemover issueRemover = new IssueRemover();
        issueRemover.deleteIssue(cookieValue, issueId);
    }

    @Test
    @Tag("CreateIssue")
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

                issueId = response.getId();
                String keyFromResponse = response.getKey();

        CreationIssueResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .get("/rest/api/2/issue/" + issueId)
                .then()
                .spec(responseSpec200)
                .extract().as(CreationIssueResponseModel.class);
                assertEquals(issueId, getResponse.getId());
                assertEquals(keyFromResponse, getResponse.getKey());
    }

    @Test
    public void addCommentTest() {
        CreationCommentRequestModel creationCommentRequestModel = new CreationCommentRequestModel();
        String body = "new comment";
        creationCommentRequestModel.setBody("new comment");
        Visibility visibility = new Visibility();
        visibility.setType("role");
        visibility.setValue("Administrators");
        creationCommentRequestModel.setVisibility(visibility);

        CreationCommentResponseModel response = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .body(creationCommentRequestModel)
                .when()
                .post("/rest/api/2/issue/" + issueId + "/comment")
                .then()
                .spec(responseSpec201)
                .extract().as(CreationCommentResponseModel.class);

        String idFromResponse = response.getId();
        String selfFromResponse = response.getSelf();
        String bodyFromResponse = response.getBody();

        CreationCommentResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .get("/rest/api/2/issue/" + issueId + "/comment/" + idFromResponse)
                .then()
                .spec(responseSpec200)
                .extract().as(CreationCommentResponseModel.class);
        assertEquals(idFromResponse, getResponse.getId());
        assertEquals(selfFromResponse, getResponse.getSelf());
        assertEquals(bodyFromResponse, body);
    }
    @Test
    public void addAttachmentsTest() {
        File file = new File("./src/test/resources/file.txt");

        Attachment[] response = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .header("X-Atlassian-Token", "no-check")
                .spec(requestSpecAttach)
                .multiPart("file", file)
                .when()
                .post("/rest/api/2/issue/" + issueId + "/attachments")
                .then()
                .spec(responseSpec200)
                .extract().as(Attachment[].class);
        assertEquals(response[0].getFilename(), file.getName());
        String idFromResponse = response[0].getId();
        String selfFromResponse = response[0].getSelf();

        AttachmentResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .get("/rest/api/2/attachment/" + idFromResponse)
                .then()
                .spec(responseSpec200)
                .extract().as(AttachmentResponseModel.class);
        assertEquals(selfFromResponse, getResponse.getSelf());
        assertEquals(file.getName(), getResponse.getFilename());
    }
    @Test
    public void updateCommentTest() {
        CreationCommentRequestModel creationCommentRequestModel = new CreationCommentRequestModel();
        String body = "new comment";
        creationCommentRequestModel.setBody(body);
        Visibility visibility = new Visibility();
        visibility.setType("role");
        visibility.setValue("Administrators");
        creationCommentRequestModel.setVisibility(visibility);

        CreationCommentResponseModel response = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .body(creationCommentRequestModel)
                .when()
                .post("/rest/api/2/issue/" + issueId + "/comment")
                .then()
                .spec(responseSpec201)
                .extract().as(CreationCommentResponseModel.class);

        String idFromResponse = response.getId();
        String updatedBody = "updated comment";
        creationCommentRequestModel.setBody(updatedBody);

        CreationCommentResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .body(creationCommentRequestModel)
                .when()
                .put("/rest/api/2/issue/" + issueId + "/comment/" + idFromResponse)
                .then()
                .spec(responseSpec200)
                .extract().as(CreationCommentResponseModel.class);
        assertEquals(updatedBody, getResponse.getBody());
    }
    @Test
    @Tag("RemoveIssue")
    public void deleteIssueTest() {
        String errorMess = "Issue Does Not Exist";

        given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .delete("/rest/api/2/issue/" + issueId)
                .then()
                .spec(responseSpec204);

        ErrorMessagesResponseModel getResponse = given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .get("/rest/api/2/issue/" + issueId)
                .then()
                .spec(responseSpec404)
                .extract().as(ErrorMessagesResponseModel.class);
        assertEquals(errorMess, getResponse.getErrorMessages()[0]);
    }
}
