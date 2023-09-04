package Jira_API.tests.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:credentials.properties"
})

public interface CredentialsConfig extends Config {
    @Key("username")
    String getUsername();

    @Key("password")
    String getPassword();
}