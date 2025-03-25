package com.assignment.github_repo_searcher.model;


// import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

// @Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @Table(name = "repositories")
public class Repo {
    // @Id
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private int stars;
    private int forks;
    private Instant lastUpdated;
}

