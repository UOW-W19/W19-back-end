# Technical Notes & Decisions

## 1. Project Structure
We have adopted a **Layered Architecture** (Technical Layering) for the Spring Boot application.
- `controller`: REST API endpoints.
- `service`: Business logic.
- `repository`: Data access interfaces.
- `entity`: JPA entities.
- `common`: Shared utilities and base classes.

## 2. Key Components

### Application Entry Point
The main application class has been renamed from `DemoApplication` to **`W19BackendApplication`** to reflect the project identity.

### BaseEntity (`com.example.demo.common.BaseEntity`)
A mapped superclass for all JPA entities to inherit from. It provides:
- **Primary Key**: `Long id` (Auto-increment `IDENTITY` strategy).
- **Auditing**: Automatic `createdAt` and `updatedAt` management using `@PrePersist` and `@PreUpdate`.

```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ... @PrePersist and @PreUpdate methods
}
```

## 3. Database Strategy
- **ORM**: Spring Data JPA.
- **DTO Pattern**: We will expose DTOs (Data Transfer Objects) via the API, not Entities directly.

## 4. Security Architecture (JWT)
The application uses **Spring Security** with a stateless **JWT (JSON Web Token)** based authentication mechanism.

### Key Security Components:
- **`JwtUtils`**: Handles token generation, parsing, and validation using the HMAC-SHA256 algorithm.
- **`JwtAuthenticationFilter`**: A custom filter that intercepts every request, extracts the Bearer token, validates it, and sets the security context.
- **`CustomUserDetailsService`**: Bridge between Spring Security and our database, loading `Profile` records as `UserDetails`.
- **`SecurityConfig`**: Configures the filter chain, enabling stateless session management and defining public/protected routes.

### Auth Flow:
1. User provides credentials to `/api/auth/register` (or `login`).
2. Server validates credentials and returns a JWT.
3. User includes JWT in the `Authorization: Bearer <token>` header for subsequent requests.
4. `JwtAuthenticationFilter` validates the token and authenticates the user for that request.
