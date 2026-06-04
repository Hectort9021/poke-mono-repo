FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY apps/backend/pokemon-ingestion/pom.xml apps/backend/pokemon-ingestion/pom.xml
RUN mvn -f apps/backend/pokemon-ingestion/pom.xml dependency:go-offline

COPY apps/backend/pokemon-ingestion/src apps/backend/pokemon-ingestion/src
RUN mvn -f apps/backend/pokemon-ingestion/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /workspace/apps/backend/pokemon-ingestion/target/pokemon-ingestion-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

USER spring:spring

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
