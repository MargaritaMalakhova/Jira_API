package Jira_API.tests.helpers;

import Jira_API.tests.TestBase;

import static Jira_API.tests.specs.Specifications.requestSpec;
import static Jira_API.tests.specs.Specifications.responseSpec204;
import static io.restassured.RestAssured.given;

public class IssueRemover extends TestBase {

    public void deleteIssue(String cookieValue, String issueId) {
        given().relaxedHTTPSValidation()
                .header("cookie", cookieValue)
                .spec(requestSpec)
                .when()
                .delete("/rest/api/2/issue/" + issueId)
                .then()
                .spec(responseSpec204);
    }


}
