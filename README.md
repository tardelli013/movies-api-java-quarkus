# Movies API with Java + Quarkus and Postgresql

## Prerequisites

  - JDK 11+ installed with JAVA_HOME configured appropriately
  - Apache Maven 3.6.2+
  - Docker and Docker Compose

## Simple run service with docker-compose

Before building the docker image run:
```
mvn package
```

Then, build the image with:
```
docker build -f src/main/docker/Dockerfile.jvm -t tardelli/movies-java-postgresql-quarkus-jvm .
```

Run MovieService and Postgresql database
```
docker-compose -f docker-compose.yml up
```
##### The swagger UI will be accessible under:
http://localhost:8080/swagger-ui/

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
mvn clean compile quarkus:dev:
```
