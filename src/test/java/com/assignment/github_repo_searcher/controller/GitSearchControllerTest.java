package com.assignment.github_repo_searcher.controller;

import com.assignment.github_repo_searcher.dto.GitSearchRequest;
import com.assignment.github_repo_searcher.dto.GitSearchResponse;
import com.assignment.github_repo_searcher.dto.RepositorySearchRes;
import com.assignment.github_repo_searcher.service.GitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class GitSearchControllerTest {

    @Mock
    private GitService gitService;

    private GitSearchController gitSearchController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        gitSearchController = new GitSearchController(gitService);
        mockMvc = MockMvcBuilders.standaloneSetup(gitSearchController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSearchGitRepo() throws Exception {
        GitSearchRequest request = new GitSearchRequest();
        request.setQuery("Spring Boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitSearchResponse response = new GitSearchResponse();
        when(gitService.fetchReposFromGit(any(), any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testFetchFromRepository() throws Exception {
        RepositorySearchRes response = new RepositorySearchRes();
        when(gitService.fetchFromRepos(any(), any(), any())).thenReturn(response);

        mockMvc.perform(get("/api/github/repositories")
                .param("language", "Java")
                .param("minStars", "100")
                .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
