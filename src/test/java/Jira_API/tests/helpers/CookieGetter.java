package Jira_API.tests.helpers;

import Jira_API.tests.TestBase;
import Jira_API.tests.models.AuthRequestModel;
import Jira_API.tests.models.CookieResponseModel;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CookieGetter extends TestBase {

    public String getCookie() {
        AuthRequestModel authRequestModel= new AuthRequestModel();
        authRequestModel.setUsername(credsConfig.getUsername());
        authRequestModel.setPassword(credsConfig.getPassword());
        CookieResponseModel response =given()
                .contentType(ContentType.JSON)
                .body(authRequestModel)
                .log().all()
                .when()
                .post("/rest/auth/1/session")
                .then()
                .log().all()
                .extract().response().as(CookieResponseModel.class);
        String cookieValue = "JSESSIONID=" + response.getSession().getValue();
        return cookieValue;
    }
}
