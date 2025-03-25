package com.assignment.github_repo_searcher.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.github_repo_searcher.model.Repo;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long> {
}