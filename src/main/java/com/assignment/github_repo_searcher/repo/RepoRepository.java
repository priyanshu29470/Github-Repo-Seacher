package com.assignment.github_repo_searcher.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assignment.github_repo_searcher.model.Repo;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long> {
   @Query("SELECT r FROM Repo r WHERE " +
       "(:language IS NULL OR r.language = :language) AND " +
       "(:minStars IS NULL OR r.stars >= :minStars)")
    List<Repo> findRepos(@Param("language") String language, 
                        @Param("minStars") Integer minStars, 
                        Sort sort);

}