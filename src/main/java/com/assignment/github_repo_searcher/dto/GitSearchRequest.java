package com.assignment.github_repo_searcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitSearchRequest {
    private String query;
    private String language;
    private String sort;
}