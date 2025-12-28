# Multi-stage build for Spring Boot application (ARM64/Raspberry Pi compatible)
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage - Using Debian-based image for ARM64 compatibility
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy the built jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
#HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]