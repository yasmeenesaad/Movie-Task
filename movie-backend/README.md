# Backend - MovieBase System

## Overview
This is the backend for the MovieBase System built with **Spring Boot** and **PostgreSQL**. It provides authentication, movie management, and user ratings. The system interacts with the **OMDb API** to fetch movie details.

## Features
- **Authentication** using JWT (Admin & User roles)
- **Movie Management** (Admin can add, delete, batch add/delete movies)
- **User Ratings** (Users can rate movies)
- **OMDb API Integration** (Fetch movie details from OMDb API)
- **Global Exception Handling**

## Tech Stack
- **Java 17**
- **Spring Boot**
- **Spring Security & JWT**
- **PostgreSQL**
- **Hibernate & JPA**

## Setup Instructions
### Prerequisites
- Install **Java 17**
- Install **PostgreSQL**
- Set up environment variables:
  ```bash
  export DATABASE_URL=YOUR_DATABASE_URL_INCLUDING_USERNAME_AND_PASSWORD
  export omdb.api.key=YOUR_OMDB_API_KEY
  ```

### Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/2yaty/Movie-Base.git
   cd Movie-Base
   ```
2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### API Endpoints
#### Authentication
- `POST /api/auth/login` → Authenticate user and return JWT token

#### Movie Management (Admin Only)
- `POST /api/movies/batch-add` → Add multiple movies
- `DELETE /api/movies/batch-delete` → Remove multiple movies

#### Movie Viewing (User/Admin)
- `GET /api/movies` → Get all movies
- `GET /api/movies/{id}` → Get movie details

#### Ratings (User Only)
- `POST /api/ratings` → Rate a movie

#### OMDb Integration
- `GET /api/omdb/search?title=movie_name` → Search for a movie in OMDb

## Deployment
- Create a **Dockerfile** & **docker-compose.yml** for easy deployment.
- Set up CI/CD with **GitHub Actions**.

## License
This project is licensed under MIT License.

