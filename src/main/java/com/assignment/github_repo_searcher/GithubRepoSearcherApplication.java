package com.assignment.github_repo_searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.assignment.github_repo_searcher")
public class GithubRepoSearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubRepoSearcherApplication.class, args);
	}

}


