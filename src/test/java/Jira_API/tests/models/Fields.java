package Jira_API.tests.models;

import lombok.Data;

@Data
public class Fields {
    private Project project;
    private String summary;
    private String description;
    private IssueType issuetype;
}

