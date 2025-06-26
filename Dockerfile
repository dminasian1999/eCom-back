FROM maven:3.8.6-eclipse-temurin-17 AS builder
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
COPY --from=builder ./target/eCom-back-0.0.1-SNAPSHOT.jar ./eCom-back.jar
EXPOSE 8080
CMD ["java", "-jar", "eCom-back.jar"]
