package com.assignment.github_repo_searcher.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepoDTO;

import reactor.core.publisher.Mono;


@Service
public class GitService {


    @Value("${git.api.token}")
    private String token;

    private String baseUrl= "https://api.github.com/search/repositories";

    private final WebClient webClient;

    public GitService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<GitSearchResponse> fetchRepos(String query, String language, String sort) {
        String url = "?q=" + query + "+language:" + language + "&sort=" + sort + "&per_page=5&page=1";

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                    List<RepoDTO> repoDTOs = items.stream()
                    .map(item -> new RepoDTO(
                            ((Number) item.get("id")).longValue(), // Convert to long
                            (String) item.get("name"),
                            (String) item.get("description"),
                            ((Map<String, Object>) item.get("owner")).get("login").toString(),
                            (String) item.get("language"),
                            ((Number) item.get("stargazers_count")).intValue(),
                            ((Number) item.get("forks_count")).intValue(),
                            (String) item.get("updated_at") // ISO 8601 format
                    ))
                    .collect(Collectors.toList());

                    return new GitSearchResponse("Repositories fetched and saved successfully", repoDTOs);
                });
    }
        
}

