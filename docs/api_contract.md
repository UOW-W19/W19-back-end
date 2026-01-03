# Spring Boot API Contract for Locale Frontend

Base URL: `http://localhost:8080/api`

## Authentication

All protected endpoints require: `Authorization: Bearer {jwt_token}`

---

## Phase 1: Core Endpoints (MVP)

### 1. Authentication

#### Register
```http
POST /api/auth/register
Content-Type: application/json
```

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "username": "johndoe",
  "displayName": "John Doe"
}
```

**Response (201 Created):**
```json
{
  "userId": "uuid-string",
  "accessToken": "jwt_token_here",
  "refreshToken": "refresh_token_here",
  "expiresIn": 3600
}
```

**Errors:**
- `400` - Validation error (weak password, invalid email)
- `409` - Email or username already exists

---

#### Login
```http
POST /api/auth/login
Content-Type: application/json
```

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "userId": "uuid-string",
  "accessToken": "jwt_token_here",
  "refreshToken": "refresh_token_here",
  "expiresIn": 3600
}
```

**Errors:**
- `401` - Invalid credentials

---

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json
```

**Request:**
```json
{
  "refreshToken": "refresh_token_here"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "new_jwt_token",
  "refreshToken": "new_refresh_token",
  "expiresIn": 3600
}
```

---

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer {token}
```

**Response:** `204 No Content`

---

### 2. User Profile

#### Get Current User
```http
GET /api/users/me
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "id": "uuid-string",
  "email": "user@example.com",
  "username": "johndoe",
  "displayName": "John Doe",
  "avatarUrl": "https://...",
  "bio": "Language enthusiast",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "languages": [
    {
      "code": "en",
      "name": "English",
      "flagEmoji": "🇬🇧",
      "proficiency": "NATIVE",
      "isLearning": false
    },
    {
      "code": "es",
      "name": "Spanish",
      "flagEmoji": "🇪🇸",
      "proficiency": "B2",
      "isLearning": true
    }
  ],
  "streak": {
    "current": 15,
    "longest": 30
  },
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

#### Update Profile
```http
PATCH /api/users/me
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "displayName": "John D.",
  "bio": "Updated bio",
  "avatarUrl": "https://...",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

**Response (200 OK):** Updated user object

---

### 3. Posts

#### Get Feed
```http
GET /api/posts?page=0&size=20&language=all
Authorization: Bearer {token}
```

**Query Parameters:**
- `page` - Page number (0-indexed)
- `size` - Items per page (default: 20)
- `language` - Filter by language code or "all"
- `latitude` - User's latitude (for distance calc)
- `longitude` - User's longitude (for distance calc)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "uuid-string",
      "author": {
        "id": "uuid-string",
        "username": "maria_garcia",
        "displayName": "María García",
        "avatarUrl": "https://...",
        "language": "Spanish",
        "flagEmoji": "🇪🇸"
      },
      "content": "¡Hola! Hoy aprendí una palabra nueva...",
      "translation": "Hello! Today I learned a new word...",
      "originalLanguage": "es",
      "imageUrl": "https://...",
      "latitude": 40.4168,
      "longitude": -3.7038,
      "location": "Madrid, Spain",
      "distance": "2.5 km",
      "reactions": {
        "likes": 24,
        "comments": 5
      },
      "userReaction": "LIKE",
      "createdAt": "2024-01-20T14:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "last": false
}
```

---

#### Create Post
```http
POST /api/posts
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "content": "¡Buenos días desde Madrid!",
  "originalLanguage": "es",
  "translation": "Good morning from Madrid!",
  "imageUrl": "https://...",
  "latitude": 40.4168,
  "longitude": -3.7038
}
```

**Response (201 Created):** Created post object

---

#### Get Single Post
```http
GET /api/posts/{postId}
Authorization: Bearer {token}
```

---

#### Delete Post
```http
DELETE /api/posts/{postId}
Authorization: Bearer {token}
```

**Response:** `204 No Content`

---

### 4. Reactions

#### Like/React to Post
```http
POST /api/posts/{postId}/reactions
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "reaction": "LIKE"
}
```

**Reaction Types:** `LIKE`, `LOVE`, `HELPFUL`, `FUNNY`

**Response (200 OK):**
```json
{
  "likes": 25,
  "comments": 5,
  "userReaction": "LIKE"
}
```

---

#### Remove Reaction
```http
DELETE /api/posts/{postId}/reactions
Authorization: Bearer {token}
```

---

### 5. Comments

#### Get Comments
```http
GET /api/posts/{postId}/comments?page=0&size=20
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "uuid-string",
      "author": {
        "id": "uuid-string",
        "username": "john_doe",
        "displayName": "John Doe",
        "avatarUrl": "https://..."
      },
      "content": "Great post!",
      "createdAt": "2024-01-20T15:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 5
}
```

---

#### Add Comment
```http
POST /api/posts/{postId}/comments
Authorization: Bearer {token}
Content-Type: application/json
```

**Request:**
```json
{
  "content": "This is really helpful, thanks!"
}
```

**Response (201 Created):** Created comment object

---

#### Delete Comment
```http
DELETE /api/posts/{postId}/comments/{commentId}
Authorization: Bearer {token}
```

---

### 6. Languages (Public)

#### Get All Languages
```http
GET /api/languages
```

**Response (200 OK):**
```json
{
  "languages": [
    {
      "code": "en",
      "name": "English",
      "nativeName": "English",
      "flagEmoji": "🇬🇧"
    },
    {
      "code": "es",
      "name": "Spanish",
      "nativeName": "Español",
      "flagEmoji": "🇪🇸"
    }
  ]
}
```

---

## Database Schema (PostgreSQL)

Use the existing `docs/database-schema.sql` file in this repo for the complete schema.

**Essential tables for MVP:**
- `profiles` - User profiles
- `user_roles` - User roles (user, moderator, admin)
- `user_languages` - Languages user speaks/learns
- `languages` - Reference table for all languages
- `posts` - User posts
- `post_translations` - Translations of posts
- `post_reactions` - Likes/reactions on posts
- `post_comments` - Comments on posts

---

## Spring Boot Entity Examples

### User/Profile Entity
```java
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    private String displayName;
    private String avatarUrl;
    private String bio;
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    @CreationTimestamp
    private Instant createdAt;
    
    @UpdateTimestamp
    private Instant updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLanguage> languages;
}
```

### Post Entity
```java
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Profile author;
    
    @Column(nullable = false)
    private String content;
    
    private String originalLanguage;
    private String imageUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.APPROVED;
    
    @CreationTimestamp
    private Instant createdAt;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostReaction> reactions;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> comments;
}
```

---

## JWT Token Structure

**Payload:**
```json
{
  "sub": "user-uuid",
  "email": "user@example.com",
  "username": "johndoe",
  "roles": ["user"],
  "iat": 1705312200,
  "exp": 1705315800
}
```

---

## Error Response Format

All errors should follow this format:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "must be a valid email address"
    }
  ]
}
```

---

## CORS Configuration

Your Spring Boot app needs to allow requests from the Lovable frontend:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:5173",  // Vite dev
                "http://localhost:8080",
                "https://*.lovableproject.com"  // Production
            )
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

---

## Next Steps

1. Implement the auth endpoints first (register, login, refresh)
2. Add the user profile endpoints
3. Implement posts CRUD
4. Add reactions and comments
5. Let me know your API URL and I'll connect the frontend!
