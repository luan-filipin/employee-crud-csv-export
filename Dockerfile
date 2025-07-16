# Etapa 1: build do app (usando Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: rodar o app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/postgreSQL-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
