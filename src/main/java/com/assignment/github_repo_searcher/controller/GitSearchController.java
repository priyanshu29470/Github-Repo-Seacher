package com.assignment.github_repo_searcher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.github_repo_searcher.dto.GitSearchRequest;
import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.service.GitService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/github")
public class GitSearchController {

    private final GitService gitService;

    public GitSearchController(GitService gitService) {
        this.gitService = gitService;
    }

    @PostMapping("/search")
    public GitSearchResponse searchGitRepo(@RequestBody GitSearchRequest req) {
        return gitService.fetchReposFromGit(req.getQuery(), req.getLanguage(), req.getSort());
    }

    @GetMapping("/repositories")
    public RepositorySearchRes fetchFromRepository(
        @RequestParam(required = false) String language,
        @RequestParam(required = false) Integer minStars,
        @RequestParam(defaultValue = "stars") String sort) {
        return gitService.fetchFromRepos(language, minStars, sort);
    }
}
