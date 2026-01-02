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
