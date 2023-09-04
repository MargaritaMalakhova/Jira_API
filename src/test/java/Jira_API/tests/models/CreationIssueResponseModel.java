package Jira_API.tests.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreationIssueResponseModel {
    String id;
    String key;
    String self;
}
