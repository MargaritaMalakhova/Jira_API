package Jira_API.tests;

import Jira_API.tests.config.CredentialsConfig;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    public CredentialsConfig credsConfig = ConfigFactory.create(CredentialsConfig.class, System.getProperties());
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
    }

}
