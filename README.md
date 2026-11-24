# AWS DevOps CI/CD Project

A Java Spring Boot application for learning
end-to-end AWS DevOps CI/CD.

## Technologies

- Java 17
- Spring Boot
- Maven
- Git
- GitHub

## API Endpoints

GET /
GET /api/health
GET /api/hello

## Build

mvn clean package

## Run

java -jar target/aws-devops-cicd-project-1.0.0.jar

## Planned CI/CD Architecture

GitHub
   |
Jenkins
   |
Maven Build and Tests
   |
SonarQube
   |
Trivy
   |
Docker
   |
AWS ECR
   |
AWS EC2
