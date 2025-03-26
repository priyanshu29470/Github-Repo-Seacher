package com.assignment.github_repo_searcher.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.helper.RepoMapper;
import com.assignment.github_repo_searcher.model.Repo;
import com.assignment.github_repo_searcher.repo.RepoRepository;

@Service
public class GitService {

    @Value("${git.api.token}")
    private String token;

    private static final String BASE_URL = "https://api.github.com/search/repositories";

    private final RestTemplate restTemplate;
    private final RepoRepository repoRepository;
    private final RepoMapper repoMapper;

    public GitService(RestTemplate restTemplate, RepoRepository repoRepository, RepoMapper repoMapper) {
        this.restTemplate = restTemplate;
        this.repoRepository = repoRepository;
        this.repoMapper = repoMapper;
    }

    public GitSearchResponse fetchReposFromGit(String query, String language, String sort) {
        String url = buildGitUrl(query, language, sort);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
            );
            
            return saveGitResponse(response.getBody());
        } catch (Exception e) {
            String errorMessage = "Failed to fetch repositories: " + e.getMessage();
            return new GitSearchResponse(errorMessage, List.of());
        }
    }

    private String buildGitUrl(String query, String language, String sort) {
        return String.format("%s?q=%s+language:%s&sort=%s&per_page=5&page=1", 
            BASE_URL, 
            query != null ? query : "", 
            language != null ? language : "", 
            sort != null ? sort : "");
    }

    private GitSearchResponse saveGitResponse(Map<String, Object> res) {
        if (res == null || !res.containsKey("items")) {
            return new GitSearchResponse("Invalid response from GitHub API", List.of());
        }
        List<Repo> repos = repoMapper.convertToRepoList(res);
        if (repos.isEmpty()) {
            return new GitSearchResponse("No repositories found for the given query", List.of());
        }
        repoRepository.saveAll(repos);
        return new GitSearchResponse("Repositories fetched and saved successfully", repoMapper.convertToRepoDTOList(repos));
    }

    public RepositorySearchRes fetchFromRepos(String language, Integer minStars, String sort) {
        String sortField = sort != null ? sort : "stars";
        List<Repo> repos = repoRepository.findRepos(language, minStars, Sort.by(Sort.Direction.DESC, sortField));
        return new RepositorySearchRes(repoMapper.convertToRepoDTOList(repos));
    }
}
