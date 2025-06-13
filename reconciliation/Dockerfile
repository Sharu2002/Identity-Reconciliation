# Build Stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper files and pom.xml
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Pre-fetch dependencies (better Docker caching)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime Stage
FROM openjdk:21-slim

WORKDIR /app

# Copy only the built JAR from build stage
COPY --from=build /app/target/*.jar reconciliation.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "reconciliation.jar"]