# Demo Spring Boot Application

A Spring Boot 3.5.9 application with JWT authentication and MySQL database integration.

## Technologies

- Java 17
- Spring Boot 3.5.9
- Spring Security 6
- Spring Data JPA
- MySQL 8
- JWT (jjwt 0.12.6)
- Maven

## Prerequisites

- JDK 17 or higher
- MySQL Server running on localhost:3306
- Maven (or use the Maven wrapper)

## Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=password
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

**Register a new user:**
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}
```

**Login:**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

Response includes JWT token:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser"
}
```

### Protected Endpoints

Use the token in the Authorization header:
```bash
Authorization: Bearer <your-jwt-token>
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   └── AuthController.java
│   │   ├── dto/
│   │   │   ├── AuthRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   └── RegisterRequest.java
│   │   ├── model/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtUtil.java
│   │   └── service/
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/demo/
```

## Security Features

- JWT-based authentication
- BCrypt password encoding
- Stateless session management
- Role-based access control (USER role by default)
- Protected endpoints requiring valid JWT token
