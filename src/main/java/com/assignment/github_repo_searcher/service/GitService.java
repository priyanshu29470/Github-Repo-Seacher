package com.assignment.github_repo_searcher.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.helper.RepoMapper;
import com.assignment.github_repo_searcher.model.Repo;
import com.assignment.github_repo_searcher.repo.RepoRepository;

import reactor.core.publisher.Mono;

@Service
public class GitService {

    @Value("${git.api.token}")
    private String token;

    private static final String BASE_URL = "https://api.github.com/search/repositories";

    private final WebClient webClient;
    private final RepoRepository repoRepository;
    private final RepoMapper repoMapper;

    public GitService(WebClient.Builder webClientBuilder, RepoRepository repoRepository, RepoMapper repoMapper) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.repoRepository = repoRepository;
        this.repoMapper = repoMapper;
    }

    public Mono<GitSearchResponse> fetchReposFromGit(String query, String language, String sort) {
        String url = buildGitUrl(query, language, sort);

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> saveGitResponse(res));
    }

    private String buildGitUrl(String query, String language, String sort) {
        return String.format("?q=%s+language:%s&sort=%s&per_page=5&page=1", query, language, sort);
    }

    private GitSearchResponse saveGitResponse(Map<String, Object> res) {
        List<Repo> repos = repoMapper.convertToRepoList(res);
        repoRepository.saveAll(repos);
        return new GitSearchResponse("Repositories fetched and saved successfully", repoMapper.convertToRepoDTOList(repos));
    }

    public RepositorySearchRes fetchFromRepos(String language, Integer minStars, String sort) {
        List<Repo> repos = repoRepository.findRepos(language, minStars, Sort.by(Sort.Direction.DESC, sort));
        return new RepositorySearchRes(repoMapper.convertToRepoDTOList(repos));
    }
}
