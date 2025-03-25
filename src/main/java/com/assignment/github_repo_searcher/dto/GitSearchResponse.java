package com.assignment.github_repo_searcher.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GitSearchResponse {
    private String message;
    private List<RepoDTO> repositories; 
}


