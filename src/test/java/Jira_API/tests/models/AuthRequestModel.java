package Jira_API.tests.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequestModel {
    String username;
    String password;
}
