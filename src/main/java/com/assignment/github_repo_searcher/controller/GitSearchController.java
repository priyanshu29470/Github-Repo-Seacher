package com.assignment.github_repo_searcher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.model.GitSearchRequest;
import com.assignment.github_repo_searcher.service.GitService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/github")
public class GitSearchController {

    @Autowired
    private GitService gitService;

    @PostMapping("/search")
    public Mono<GitSearchResponse> searchGitRepo(@RequestBody GitSearchRequest req) {
        return gitService.fetchRepos(req.getQuery(), req.getLanguage(), req.getSort());
        
    }
    
}
