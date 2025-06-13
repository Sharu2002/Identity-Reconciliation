FROM eclipse-temurin:21-jdk AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /target/reconciliation-0.0.1-SNAPSHOT.jar reconciliation.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","reconciliation.jar"]