# API Reference - W19 Backend

**Base URL:** `http://localhost:8081/api`  
**Last Updated:** 2026-01-10

> **Note:** All JSON fields are **`snake_case`**.

---

## Table of Contents

1. [Authentication](#authentication)
2. [Users & Profiles](#users--profiles)
3. [Languages](#languages)
4. [Posts & Content](#posts--content)
5. [Learning Core](#learning-core)

---

## Authentication

### POST /auth/register
Register a new user account.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "username": "johndoe",
  "display_name": "John Doe"
}
```

**Response:** `201 Created`
```json
{
  "user_id": "uuid",
  "access_token": "jwt_token",
  "refresh_token": "refresh_token",
  "expires_in": 3600
}
```

### POST /auth/login
Authenticate and receive tokens.

### POST /auth/refresh
Refresh access token.
```json
{ "refresh_token": "token" }
```

### POST /auth/logout
Invalidate session (client-side).

---

## Users & Profiles

### GET /users/me
Get current user's profile.

**Response:**
```json
{
  "id": "uuid",
  "username": "johndoe",
  "display_name": "John Doe",
  "notifications_prefs": { ... },
  "languages": [
    {
      "code": "en",
      "proficiency": "NATIVE",
      "flag_emoji": "🇬🇧",
      "is_learning": false
    }
  ]
}
```

### GET /users/me/settings
### PATCH /users/me/settings

### PUT /users/me/languages
Update user languages (native/learning).
```json
[
  { "code": "en", "proficiency": "NATIVE", "is_learning": false },
  { "code": "es", "proficiency": "BEGINNER", "is_learning": true }
]
```

### POST /users/{id}/follow
### DELETE /users/{id}/follow

### POST /users/{id}/block
### DELETE /users/{id}/block

### POST /users/{id}/follow
Follow a user.

### POST /users/{id}/unfollow
Unfollow a user (also DELETE /users/{id}/follow).

### GET /users/{id}/followers
List followers.

### GET /users/{id}/following
List following.

---

## Languages

### GET /languages
List all supported languages.

---

## Posts & Content

### GET /posts
Get the feed.
**Params:** `page`, `size`, `language`, `latitude`, `longitude`

### POST /posts
Create a post.
```json
{
  "content": "Hello",
  "original_language": "en"
}
```

### GET /posts/{id}/translations
Get translations for a post.
**Params:** `target_language` (e.g., `es`)

**Response:**
```json
{
  "language_code": "es",
  "translated_content": "Hola"
}
```

### POST /posts/{id}/comments
### GET /posts/{id}/comments

### POST /posts/{id}/reactions
### DELETE /posts/{id}/reactions

### POST /posts/{id}/reports
Report a post.
**Reasons:** `SPAM`, `HARASSMENT`, `INAPPROPRIATE`, `MISINFORMATION`, `OTHER`

---

## Learning Core (Base: `/api/learn`)

### POST /api/learn/sessions/start
Start a session.
```json
{ "session_size": 10, "language_code": "es" }
```

### POST /api/learn/sessions/{id}/submit
Submit an answer.
```json
{ "word_id": "uuid", "is_correct": true }
```

### POST /api/learn/sessions/{id}/complete
Finish session and get summary.

### GET /api/learn/sessions
Get history.

### GET /api/words
Get vocabulary list.

### POST /api/words
Save a word manually.

### GET /api/learn/stats
Get learning statistics (XP, streaks, etc).

