package com.veracode.storypointer.model;

import lombok.Data;

import java.util.List;

@Data
public class JiraIssue {
    private String jiraId;
    private String reportedBy;
    private String summary;
    private String description;
    private String priority;
    private String status;
    private List<Comment> comments;
    private List<String> blockedBy;
    private List<String> blocks;
}
