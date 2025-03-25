package com.assignment.github_repo_searcher.model;


// import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "repositories")
public class Repo {
    @Id
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private int stars;
    private int forks;
    private Instant lastUpdated;
}

