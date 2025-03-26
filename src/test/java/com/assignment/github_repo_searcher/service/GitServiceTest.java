package com.assignment.github_repo_searcher.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.helper.RepoMapper;
import com.assignment.github_repo_searcher.model.Repo;
import com.assignment.github_repo_searcher.repo.RepoRepository;

class GitServiceTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private RepoRepository repoRepository;
    
    @Mock
    private RepoMapper repoMapper;

    @InjectMocks
    private GitService gitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testFetchReposFromGit_Success() {
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

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        when(repoMapper.convertToRepoList(any())).thenReturn(List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, null)
        ));
        when(repoMapper.convertToRepoDTOList(any())).thenReturn(List.of());

        GitSearchResponse response = gitService.fetchReposFromGit("test", "Java", "stars");

        assertNotNull(response);
        assertEquals("Repositories fetched and saved successfully", response.getMessage());
        verify(repoRepository).saveAll(any());
    }

    @Test
    void testFetchReposFromGit_RateLimitExceeded() {
        Map<String, Object> mockErrorResponse = Map.of("message", "API rate limit exceeded");

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenThrow(new RuntimeException("GitHub API error: API rate limit exceeded"));

        GitSearchResponse response = gitService.fetchReposFromGit("test", "Java", "stars");

        assertNotNull(response);
        assertEquals("Failed to fetch repositories: GitHub API error: API rate limit exceeded", response.getMessage());
        verify(repoRepository, never()).saveAll(any());
    }

    @Test
    void testFetchReposFromGit_EmptyResponse() {
        Map<String, Object> mockResponse = Map.of("items", List.of());

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        when(repoMapper.convertToRepoList(any())).thenReturn(List.of());

        GitSearchResponse response = gitService.fetchReposFromGit("test", "Java", "stars");

        assertNotNull(response);
        assertEquals("No repositories found for the given query", response.getMessage());
        assertTrue(response.getRepositories().isEmpty());
        verify(repoRepository, never()).saveAll(any());
    }

    @Test
    void testFetchReposFromGit_InvalidResponse() {
        Map<String, Object> mockResponse = Map.of("invalid_key", "value");

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        GitSearchResponse response = gitService.fetchReposFromGit("test", "Java", "stars");

        assertNotNull(response);
        assertEquals("Invalid response from GitHub API", response.getMessage());
        assertTrue(response.getRepositories().isEmpty());
        verify(repoRepository, never()).saveAll(any());
    }

    @Test
    void testFetchFromRepos_WithFilters() {
        List<Repo> mockRepos = List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, null),
            new Repo(12346L, "test-repo-2", "Test description 2", "test-owner", "Java", 200, 75, null)
        );

        when(repoRepository.findRepos("Java", 100, Sort.by(Sort.Direction.DESC, "stars"))).thenReturn(mockRepos);
        when(repoMapper.convertToRepoDTOList(mockRepos)).thenReturn(List.of());

        RepositorySearchRes response = gitService.fetchFromRepos("Java", 100, "stars");

        assertNotNull(response);
        assertNotNull(response.getRepositories());
        verify(repoRepository).findRepos("Java", 100, Sort.by(Sort.Direction.DESC, "stars"));
    }

    @Test
    void testFetchFromRepos_WithoutFilters() {
        List<Repo> mockRepos = List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, null)
        );

        when(repoRepository.findRepos(null, null, Sort.by(Sort.Direction.DESC, "stars"))).thenReturn(mockRepos);
        when(repoMapper.convertToRepoDTOList(mockRepos)).thenReturn(List.of());

        RepositorySearchRes response = gitService.fetchFromRepos(null, null, "stars");

        assertNotNull(response);
        assertNotNull(response.getRepositories());
        verify(repoRepository).findRepos(null, null, Sort.by(Sort.Direction.DESC, "stars"));
    }

    @Test
    void testFetchFromRepos_WithNullSort() {
        List<Repo> mockRepos = List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, null)
        );

        when(repoRepository.findRepos("Java", 100, Sort.by(Sort.Direction.DESC, "stars"))).thenReturn(mockRepos);
        when(repoMapper.convertToRepoDTOList(mockRepos)).thenReturn(List.of());

        RepositorySearchRes response = gitService.fetchFromRepos("Java", 100, null);

        assertNotNull(response);
        assertNotNull(response.getRepositories());
        verify(repoRepository).findRepos("Java", 100, Sort.by(Sort.Direction.DESC, "stars"));
    }

    @Test
    void testFetchReposFromGit_WithNullParameters() {
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

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        when(repoMapper.convertToRepoList(any())).thenReturn(List.of(
            new Repo(12345L, "test-repo", "Test description", "test-owner", "Java", 100, 50, null)
        ));
        when(repoMapper.convertToRepoDTOList(any())).thenReturn(List.of());

        GitSearchResponse response = gitService.fetchReposFromGit(null, null, null);

        assertNotNull(response);
        assertEquals("Repositories fetched and saved successfully", response.getMessage());
        verify(repoRepository).saveAll(any());
    }
}