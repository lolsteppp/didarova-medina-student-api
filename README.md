# didarova-medina-student-api

Spring Boot CRUD application for managing students (DevOps Midterm).

- **Student:** Didarova Medina
- **Student ID:** 37944
- **Group:** IT2-2313
- **Stack:** Java 17, Spring Boot 3, Gradle, PostgreSQL 16, Docker, Docker Compose, Jenkins

Status: v1.0 released - CRUD complete and documented (Didarova Medina, ID 37944)

## API endpoints

| Method | URL                   | Description              |
|--------|-----------------------|--------------------------|
| GET    | `/api/info`           | Author and version info  |
| GET    | `/api/students`       | List all students        |
| GET    | `/api/students/{id}`  | Get one student          |
| POST   | `/api/students`       | Create a student         |
| PUT    | `/api/students/{id}`  | Update a student         |
| DELETE | `/api/students/{id}`  | Delete a student         |

## Change log

- **feature/didarova-medina-crud** (Didarova Medina): added the **UPDATE** operation
  (`PUT /api/students/{id}`). It validates the request body and returns
  `404 Not Found` with a JSON error when the student does not exist. Covered by tests.

## How to run

```bash
cp .env.example .env            # set your own POSTGRES_PASSWORD
docker compose up -d --build    # app: http://localhost:8080/api/info
./scripts/Didarova_Medina_api_test.sh
```

- Docker image: `didarova-medina-student-api:v1.0-didarova-medina-37944`
- Compose project: `didarova-medina-student-api` (services `didarova-app`, `didarova-db`)
- Jenkins job: `Didarova_Medina_Student_API` (see `Jenkinsfile`, local Jenkins in `jenkins/`)
