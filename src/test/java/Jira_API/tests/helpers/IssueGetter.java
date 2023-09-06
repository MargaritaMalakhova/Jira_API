package Jira_API.tests.helpers;

import Jira_API.tests.TestBase;
import Jira_API.tests.models.*;

import static Jira_API.tests.specs.Specifications.requestSpec;
import static Jira_API.tests.specs.Specifications.responseSpec201;
import static io.restassured.RestAssured.given;

public class IssueGetter extends TestBase {
    public String createIssue() {
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
        CookieGetter cookieGetter = new CookieGetter();

        CreationIssueResponseModel response = given().relaxedHTTPSValidation()
                .header("cookie", cookieGetter.getCookie())
                .spec(requestSpec)
                .body(creationIssueRequestModel)
                .when()
                .post("/rest/api/2/issue")
                .then()
                .spec(responseSpec201)
                .extract().as(CreationIssueResponseModel.class);

        String idFromResopnse = response.getId();
        return idFromResopnse;
    }
}
