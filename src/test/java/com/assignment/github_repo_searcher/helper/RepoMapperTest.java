package com.assignment.github_repo_searcher.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.assignment.github_repo_searcher.dto.RepoDTO;
import com.assignment.github_repo_searcher.model.Repo;

class RepoMapperTest {

    private RepoMapper repoMapper;

    @BeforeEach
    void setUp() {
        repoMapper = new RepoMapper();
    }

    @Test
    void testConvertToRepoList() {
        Map<String, Object> mockResponse = Map.of(
            "items", List.of(Map.of(
                "id", 12345,
                "name", "test-repo",
                "description", "Test description",
                "owner", Map.of("login", "test-owner"),
                "language", "Java",
                "stargazers_count", 100,
                "forks_count", 50,
                "updated_at", "2024-03-20T12:34:56Z"
            ))
        );

        List<Repo> repos = repoMapper.convertToRepoList(mockResponse);

        assertNotNull(repos);
        assertEquals(1, repos.size());
        Repo repo = repos.get(0);
        assertEquals(12345L, repo.getId());
        assertEquals("test-repo", repo.getName());
        assertEquals("Test description", repo.getDescription());
        assertEquals("test-owner", repo.getOwner());
        assertEquals("Java", repo.getLanguage());
        assertEquals(100, repo.getStars());
        assertEquals(50, repo.getForks());
        assertNotNull(repo.getLastUpdated());
    }

    @Test
    void testConvertToRepoDTOList() {
        Instant now = Instant.now();
        List<Repo> repos = List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, now)
        );

        List<RepoDTO> dtos = repoMapper.convertToRepoDTOList(repos);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        RepoDTO dto = dtos.get(0);
        assertEquals(12345L, dto.getId());
        assertEquals("test-repo", dto.getName());
        assertEquals("Test description", dto.getDescription());
        assertEquals("test-owner", dto.getOwner());
        assertEquals("Java", dto.getLanguage());
        assertEquals(100, dto.getStars());
        assertEquals(50, dto.getForks());
        assertEquals(now.toString(), dto.getLastUpdated());
    }

    @Test
    void testConvertToRepoListWithEmptyResponse() {
        Map<String, Object> mockResponse = Map.of("items", List.of());
        List<Repo> repos = repoMapper.convertToRepoList(mockResponse);
        assertNotNull(repos);
        assertTrue(repos.isEmpty());
    }

    @Test
    void testConvertToRepoDTOListWithEmptyList() {
        List<Repo> repos = List.of();
        List<RepoDTO> dtos = repoMapper.convertToRepoDTOList(repos);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
} 