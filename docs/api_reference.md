# API Reference - W19 Backend

**Base URL:** `http://localhost:8081/api`  
**Last Updated:** 2026-01-10

---

## Table of Contents

1. [Authentication](#authentication) - 4 endpoints
2. [Users & Profiles](#users--profiles) - 13 endpoints
3. [Languages](#languages) - 2 endpoints
4. [Posts & Content](#posts--content) - 9 endpoints
5. [Social Features](#social-features) - 2 endpoints
6. [Learning Core](#learning-core) - 10 endpoints

**Total Endpoints:** 40

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

---

### POST /auth/login
Authenticate and receive tokens.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:** `200 OK` (same as register)

---

### POST /auth/refresh
Refresh access token using refresh token.

**Request Body:**
```json
{
  "refresh_token": "refresh_token"
}
```

**Response:** `200 OK` (new tokens)

---

### POST /auth/logout
**Auth Required:** Bearer Token

Invalidate refresh token and logout.

**Response:** `204 No Content`

---

## Users & Profiles

### GET /users/me
**Auth Required:** Bearer Token

Get current user's profile.

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "username": "johndoe",
  "email": "user@example.com",
  "display_name": "John Doe",
  "avatar_url": "https://...",
  "bio": "Language enthusiast",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "created_at": "2024-01-15T10:30:00Z",
  "languages": [...],
  "roles": ["USER"],
  "followers_count": 150,
  "following_count": 75,
  "posts_count": 42
}
```

---

### GET /users/{user_id}
**Auth Required:** Bearer Token

Get public profile of any user.

**Response:** `200 OK` (same structure as /users/me)

---

### PATCH /users/me
**Auth Required:** Bearer Token

Update current user's profile (partial updates supported).

**Request Body:**
```json
{
  "display_name": "John D.",
  "bio": "Updated bio",
  "avatar_url": "https://...",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

**Response:** `200 OK` (updated profile)

---

### GET /users/me/settings
**Auth Required:** Bearer Token

Get user settings.

**Response:** `200 OK`
```json
{
  "notification_prefs": {
    "push_enabled": true,
    "email_enabled": true,
    "like_notifications": true,
    "comment_notifications": true,
    "meetup_notifications": true
  },
  "privacy_settings": {
    "show_location": true,
    "allow_messages": "everyone"
  },
  "theme": "dark"
}
```

---

### PATCH /users/me/settings
**Auth Required:** Bearer Token

Update user settings (partial updates supported).

**Request Body:** (same structure as GET response)

**Response:** `200 OK` (updated settings)

---

### POST /users/{user_id}/follow
**Auth Required:** Bearer Token

Follow a user.

**Response:** `200 OK`

---

### DELETE /users/{user_id}/follow
**Auth Required:** Bearer Token

Unfollow a user.

**Response:** `200 OK`

---

### GET /users/{user_id}/followers
**Auth Required:** Bearer Token

Get paginated list of user's followers.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "uuid",
      "username": "follower1",
      "display_name": "Follower One",
      "avatar_url": "https://..."
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "size": 20,
  "number": 0
}
```

---

### GET /users/{user_id}/following
**Auth Required:** Bearer Token

Get paginated list of users this user follows.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Response:** `200 OK` (same structure as followers)

---

### GET /users/me/languages
**Auth Required:** Bearer Token

Get current user's language preferences.

**Response:** `200 OK`
```json
[
  {
    "code": "en",
    "name": "English",
    "flag_emoji": "🇬🇧",
    "proficiency": "NATIVE",
    "is_learning": false
  },
  {
    "code": "es",
    "name": "Spanish",
    "flag_emoji": "🇪🇸",
    "proficiency": "INTERMEDIATE",
    "is_learning": true
  }
]
```

---

### PUT /users/me/languages
**Auth Required:** Bearer Token

Update user's language preferences (full replacement).

**Request Body:**
```json
[
  {
    "code": "en",
    "proficiency": "NATIVE",
    "is_learning": false
  },
  {
    "code": "es",
    "proficiency": "BEGINNER",
    "is_learning": true
  }
]
```

**Response:** `200 OK` (updated languages)

---

### POST /users/{user_id}/block
**Auth Required:** Bearer Token

Block a user.

**Response:** `200 OK`

---

### DELETE /users/{user_id}/block
**Auth Required:** Bearer Token

Unblock a user.

**Response:** `200 OK`

---

## Languages

### GET /languages
Get all available system languages.

**Response:** `200 OK`
```json
{
  "languages": [
    {
      "code": "en",
      "name": "English",
      "native_name": "English",
      "flag_emoji": "🇬🇧"
    },
    {
      "code": "es",
      "name": "Spanish",
      "native_name": "Español",
      "flag_emoji": "🇪🇸"
    }
  ]
}
```

---

## Posts & Content

### GET /posts
**Auth Required:** Bearer Token

Get paginated feed of posts.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)
- `language` (optional: filter by language code or "all")
- `latitude` (optional: for distance calculation)
- `longitude` (optional: for distance calculation)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "uuid",
      "content": "Hello world!",
      "original_language": "en",
      "image_url": null,
      "latitude": 40.7128,
      "longitude": -74.0060,
      "location": "Nearby",
      "distance": "5.2 km",
      "status": "ACTIVE",
      "created_at": "2024-01-15T10:30:00Z",
      "author": {
        "id": "uuid",
        "username": "johndoe",
        "display_name": "John Doe",
        "avatar_url": "https://...",
        "language": "English",
        "flag_emoji": "🇬🇧"
      },
      "reactions": {
        "likes": 42,
        "comments": 10
      },
      "user_reaction": "LIKE"
    }
  ],
  "totalElements": 100,
  "totalPages": 5
}
```

**Note:** `status` field can be: `ACTIVE`, `PENDING_REVIEW`, `HIDDEN`, `REMOVED`

---

### POST /posts
**Auth Required:** Bearer Token

Create a new post.

**Request Body:**
```json
{
  "content": "Hello world!",
  "original_language": "en",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "image_url": "https://..."
}
```

**Response:** `201 Created` (PostResponse)

---

### GET /posts/{post_id}
**Auth Required:** Bearer Token

Get a single post by ID.

**Response:** `200 OK` (PostResponse)

---

### DELETE /posts/{post_id}
**Auth Required:** Bearer Token

Delete a post (must be author).

**Response:** `204 No Content`

---

### POST /posts/{post_id}/translations
**Auth Required:** Bearer Token

Request translation of a post.

**Request Body:**
```json
{
  "target_language": "es"
}
```

**Response:** `200 OK`
```json
{
  "language_code": "es",
  "translated_content": "¡Hola mundo!"
}
```

---

### POST /posts/{post_id}/reports
**Auth Required:** Bearer Token

Report a post.

**Request Body:**
```json
{
  "reason": "SPAM",
  "description": "This is spam content"
}
```

**Valid reasons:** `SPAM`, `HARASSMENT`, `INAPPROPRIATE_CONTENT`, `MISINFORMATION`, `OTHER`

**Response:** `201 Created`

---

### GET /posts/{post_id}/comments
**Auth Required:** Bearer Token

Get comments for a post.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "uuid",
      "content": "Great post!",
      "parent_comment_id": null,
      "created_at": "2024-01-15T10:35:00Z",
      "author": {
        "id": "uuid",
        "username": "commenter",
        "display_name": "Commenter",
        "avatar_url": "https://..."
      }
    }
  ]
}
```

**Note:** `parent_comment_id` enables threaded comments (replies).

---

### POST /posts/{post_id}/comments
**Auth Required:** Bearer Token

Add a comment to a post.

**Request Body:**
```json
{
  "content": "Great post!",
  "parent_comment_id": null
}
```

**Response:** `201 Created` (CommentResponse)

---

### DELETE /posts/{post_id}/comments/{comment_id}
**Auth Required:** Bearer Token

Delete a comment (must be author).

**Response:** `204 No Content`

---

## Social Features

### POST /posts/{post_id}/reactions
**Auth Required:** Bearer Token

Add or update reaction to a post.

**Request Body:**
```json
{
  "reaction_type": "LIKE"
}
```

**Valid types:** `LIKE`, `LOVE`, `HELPFUL`, `FUNNY`

**Response:** `200 OK`

---

### DELETE /posts/{post_id}/reactions/{reaction_type}
**Auth Required:** Bearer Token

Remove a reaction from a post.

**Response:** `204 No Content`

---

## Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `204 No Content` - Request successful, no content to return
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Authenticated but not authorized
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Authentication

All endpoints except `/auth/register`, `/auth/login`, and `/languages` require authentication.

**Header:**
```
Authorization: Bearer {access_token}
```

**Token Expiry:** 1 hour  
**Refresh Token:** Use `/auth/refresh` to get new tokens

---

## Learning Core

### GET /words
Get all saved words for the authenticated user.

**Query Parameters:**
- `language` (optional) - Filter by language code
- `sort` (optional) - Sort order: `newest`, `mastery_high`, `mastery_low`
- `page` (optional, default: 0)
- `size` (optional, default: 50)

**Response:** `200 OK` - Paginated list
```json
{
  "content": [
    {
      "id": "uuid",
      "word": "さくら",
      "translation": "cherry blossom",
      "language_code": "ja",
      "language_name": "Japanese",
      "language_flag": "🇯🇵",
      "source": "POST",
      "mastery_level": 75,
      "created_at": "2024-01-10T08:30:00Z"
    }
  ],
  "totalElements": 45,
  "totalPages": 1
}
```

---

### POST /words
Save a new word.

**Request Body:**
```json
{
  "word": "さくら",
  "translation": "cherry blossom",
  "language_code": "ja",
  "source": "POST",
  "source_id": "post-uuid",
  "source_context": "Spring festival post"
}
```

**Response:** `201 Created`

**Errors:**
- `409 Conflict` - Word already saved

---

### PATCH /words/{wordId}
Update a saved word.

**Request Body:**
```json
{
  "translation": "sakura flower"
}
```

**Response:** `200 OK`

---

### DELETE /words/{wordId}
Delete a saved word.

**Response:** `204 No Content`

---

### POST /practice/sessions
Start a new practice session.

**Request Body:**
```json
{
  "session_size": 10,
  "language_code": "ja"
}
```

**Response:** `201 Created`
```json
{
  "session_id": "uuid",
  "words": [
    {
      "id": "uuid",
      "word": "さくら",
      "translation": "cherry blossom",
      "language_code": "ja",
      "language_flag": "🇯🇵",
      "mastery_level": 50
    }
  ],
  "started_at": "2024-01-10T08:30:00Z"
}
```

**Errors:**
- `400 Bad Request` - Invalid session size or not enough words

---

### POST /practice/sessions/{sessionId}/results
Submit a practice result.

**Request Body:**
```json
{
  "word_id": "uuid",
  "is_correct": true,
  "response_time_ms": 2500
}
```

**Response:** `200 OK`
```json
{
  "word_id": "uuid",
  "is_correct": true,
  "new_mastery_level": 60,
  "mastery_change": 10
}
```

---

### POST /practice/sessions/{sessionId}/complete
Complete a practice session.

**Response:** `200 OK`
```json
{
  "session_id": "uuid",
  "words_practiced": 10,
  "correct_count": 7,
  "accuracy": 70,
  "duration_seconds": 180,
  "results": [
    {
      "word_id": "uuid",
      "word": "さくら",
      "is_correct": true,
      "old_mastery": 50,
      "new_mastery": 60
    }
  ]
}
```

---

### GET /practice/sessions
Get practice history.

**Query Parameters:**
- `page` (optional, default: 0)
- `size` (optional, default: 20)

**Response:** `200 OK` - Paginated list of sessions

---

### GET /learn/stats
Get learning statistics.

**Response:** `200 OK`
```json
{
  "total_words": 45,
  "average_mastery": 62,
  "languages": [
    {
      "code": "ja",
      "name": "Japanese",
      "flag": "🇯🇵",
      "word_count": 20,
      "average_mastery": 75
    }
  ],
  "mastery_distribution": {
    "beginner": 10,
    "learning": 15,
    "familiar": 12,
    "mastered": 8
  }
}
```

---

## Notes

- All timestamps are in ISO 8601 format
- All IDs are UUIDs
- All responses use `snake_case` field names
- Pagination uses zero-based page numbers
- Default page size is 20 (configurable per endpoint)
- Mastery scale: 0-100
- Session sizes: 5, 10, or 15 only
