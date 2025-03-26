# GitHub Repository Searcher

## Overview
GitHub Repository Searcher is a Spring Boot application that allows users to search for GitHub repositories using the GitHub REST API. The application stores search results in a PostgreSQL database and provides API endpoints to retrieve stored results based on filter criteria.

## Prerequisites
Before running the application, ensure you have the following installed:

- **Java 17** or later
- **Maven** (if using `mvn` instead of `./mvnw`)
- **Docker & Docker Compose** (for PostgreSQL database setup)
- **GitHub API Token** (for accessing GitHub API)

## Installation and Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/priyanshu29470/Github-Repo-Seacher.git
   cd github-repo-searcher
   ```

2. **Set up GitHub API Token:**
   - Get your GitHub API token from [GitHub Developer Settings](https://github.com/settings/tokens)
   - Set it as an environment variable:
     ```sh
     export GIT_API_TOKEN=your_github_token
     ```

3. **Give execution permissions to scripts:**
   ```sh
   chmod +x run.sh test.sh
   ```

4. **Run the application:**
   ```sh
   ./run.sh
   ```
   This will:
   - Start the PostgreSQL database using Docker
   - Build and run the Spring Boot application

## Running Tests and Generating Code Coverage Report
To execute test cases and generate a JaCoCo coverage report:

```sh
./test.sh
```

After execution, the code coverage report can be found at:
```
target/site/jacoco/index.html
```

## API Endpoints

### 1. Search GitHub Repositories
- **Endpoint:** `POST /api/github/search`
- **Description:** Fetches repository details from GitHub based on search criteria and stores them in the database.
- **Request Body:**
  ```json
  {
    "query": "spring boot",
    "language": "Java",
    "sort": "stars"
  }
  ```
- **Response Example:**
  ```json
  {
    "message": "Repositories fetched and saved successfully",
    "repositories": [
      {
        "id": 123456,
        "name": "spring-boot-example",
        "description": "An example repository for Spring Boot",
        "owner": "user123",
        "language": "Java",
        "stars": 450,
        "forks": 120,
        "lastUpdated": "2024-01-01T12:00:00Z"
      }
    ]
  }
  ```

### 2. Retrieve Stored Results
- **Endpoint:** `GET /api/github/repositories`
- **Description:** Retrieves stored repository details based on optional filters.
- **Query Parameters:**
  - `language` (optional): Filter by programming language
  - `minStars` (optional): Minimum stars count
  - `sort` (optional): Sort by stars, forks, or updated (default: stars)
- **Example Request:**
  ```sh
  GET /api/github/repositories?language=Java&minStars=100&sort=stars
  ```
- **Response Example:**
  ```json
  {
    "repositories": [
      {
        "id": 123456,
        "name": "spring-boot-example",
        "description": "An example repository for Spring Boot",
        "owner": "user123",
        "language": "Java",
        "stars": 450,
        "forks": 120,
        "lastUpdated": "2024-01-01T12:00:00Z"
      }
    ]
  }
  ```
