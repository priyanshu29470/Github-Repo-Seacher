package com.assignment.github_repo_searcher.helper;


import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.assignment.github_repo_searcher.dto.RepoDTO;
import com.assignment.github_repo_searcher.model.Repo;

@Component
public class RepoMapper {

    public List<Repo> convertToRepoList(Map<String, Object> response) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

        return items.stream()
                .map(this::mapToRepo)
                .toList();
    }

    private Repo mapToRepo(Map<String, Object> item) {
        return new Repo(
                ((Number) item.get("id")).longValue(),
                (String) item.get("name"),
                (String) item.get("description"),
                ((Map<String, Object>) item.get("owner")).get("login").toString(),
                (String) item.get("language"),
                ((Number) item.get("stargazers_count")).intValue(),
                ((Number) item.get("forks_count")).intValue(),
                Instant.parse((String) item.get("updated_at"))
        );
    }

    public List<RepoDTO> convertToRepoDTOList(List<Repo> repos) {
        return repos.stream()
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
    }
}

