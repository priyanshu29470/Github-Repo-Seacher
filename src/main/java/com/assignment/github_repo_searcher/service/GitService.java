package com.assignment.github_repo_searcher.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepoDTO;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.model.Repo;
import com.assignment.github_repo_searcher.repo.RepoRepository;

import reactor.core.publisher.Mono;


@Service
public class GitService {


    @Value("${git.api.token}")
    private String token;

    private String baseUrl= "https://api.github.com/search/repositories";

    private final WebClient webClient;
    private final RepoRepository repoRepository;

    public GitService(WebClient.Builder webClientBuilder, RepoRepository repoRepository) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.repoRepository = repoRepository;

    }

    public Mono<GitSearchResponse> fetchReposFromGit(String query, String language, String sort) {
        String url = "?q=" + query + "+language:" + language + "&sort=" + sort + "&per_page=5&page=1";

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                     List<Repo> repos = items.stream()
                            .map(item -> new Repo(
                                    ((Number) item.get("id")).longValue(),
                                    (String) item.get("name"),
                                    (String) item.get("description"),
                                    ((Map<String, Object>) item.get("owner")).get("login").toString(),
                                    (String) item.get("language"),
                                    ((Number) item.get("stargazers_count")).intValue(),
                                    ((Number) item.get("forks_count")).intValue(),
                                    Instant.parse((String) item.get("updated_at")) 
                            ))
                            .toList();
                    repoRepository.saveAll(repos); 

                    List<RepoDTO> repoDTOs = repos.stream()
                            .map(repo -> new RepoDTO(
                                    repo.getId(),
                                    repo.getName(),
                                    repo.getDescription(),
                                    repo.getOwner(),
                                    repo.getLanguage(),
                                    repo.getStars(),
                                    repo.getForks(),
                                    repo.getLastUpdated().toString()
                            ))
                            .toList();

                    return new GitSearchResponse("Repositories fetched and saved successfully", repoDTOs);
                });
    }

    public RepositorySearchRes fetchFromRepos(String language, Integer minStars, String sort) {
        List<Repo> repos = repoRepository.findRepos(language, minStars, Sort.by(Sort.Direction.DESC, sort));
        List<RepoDTO> repoDTOs = repos.stream()
        .map(repo -> new RepoDTO(
                repo.getId(),
                repo.getName(),
                repo.getDescription(),
                repo.getOwner(),
                repo.getLanguage(),
                repo.getStars(),
                repo.getForks(),
                repo.getLastUpdated().toString()
        ))
        .toList();

        return new RepositorySearchRes(repoDTOs);
    }
        
}

