# Frontend Integration Guide (JWT)

This guide explains how to connect your frontend application to the Backend.

## 1. Authentication Flow

### Register & Login
**Endpoints**: `POST /api/auth/register`, `POST /api/auth/login`
**Response**:
```json
{
  "userId": "uuid-string",
  "accessToken": "jwt_token",
  "refreshToken": "uuid-string",
  "expiresIn": 3600
}
```

### Refresh Token
When the `accessToken` expires, call this to get a new pair.
**Endpoint**: `POST /api/auth/refresh`
**Body**: `{ "refreshToken": "stored_uuid" }`

---

## 2. API Authorization
Include the `accessToken` in the `Authorization` header for all protected requests:
`Authorization: Bearer <your_token>`

---

## 3. Core Features

### User Profile
- **Current User**: `GET /api/users/me` (Legacy `/api/profiles/me` also supported)
- **Update Profile**: `PATCH /api/users/me`

### Community Feed
- **Get Feed**: `GET /api/posts?page=0&size=20&language=all`
  - Optional params: `language`, `latitude`, `longitude` (for distance calc).
- **Create Post**: `POST /api/posts`
  - Body: `{ "content": "Hello!", "originalLanguage": "en", "latitude": 4.5, "longitude": -1.2 }`

### Social Interactions
- **Reactions**: `POST /api/posts/{postId}/reactions` (Body: `{ "reaction": "LIKE" }`)
- **Comments**: `GET /api/posts/{postId}/comments` or `POST /api/posts/{postId}/comments`

---

## 4. API Reference & Types

### **POSTS**

#### `GET /api/posts` (The Feed)
Returns a paginated list of posts.
- **Query Params**: `page` (0-index), `size` (default 20), `language`, `latitude`, `longitude`
- **Response**:
```json
{
  "content": [
    {
      "id": "uuid-string",
      "content": "Hello World",
      "originalLanguage": "en",
      "imageUrl": "http://...",
      "latitude": 40.7128,
      "longitude": -74.0060,
      "distance": "5.2 km",  // "Unknown" if location off
      "location": "Nearby",  // Human readable label
      "createdAt": "2024-01-01T12:00:00Z",
      "author": {
        "id": "uuid-string",
        "username": "jdoe",
        "displayName": "John Doe",
        "avatarUrl": "http://...",
        "language": "English",
        "flagEmoji": "🇬🇧"
      },
      "reactions": {
        "likes": 10,
        "comments": 5
      },
      "userReaction": "LIKE" // or null
    }
  ],
  "pageable": {},
  "last": false,
  "totalPages": 5,
  "totalElements": 100
}
```

#### `POST /api/posts`
Create a new post.
- **Request**:
```json
{
  "content": "My new post",
  "originalLanguage": "vi",
  "imageUrl": "optional-url",
  "latitude": 10.8231,
  "longitude": 106.6297
}
```

---

### **COMMENTS**

#### `GET /api/posts/{postId}/comments`
- **Response**:
```json
{
  "content": [
    {
      "id": "uuid-string",
      "content": "Great post!",
      "createdAt": "...",
      "author": {
        "id": "uuid-string",
        "username": "alice",
        "displayName": "Alice",
        "avatarUrl": "..."
      }
    }
  ]
}
```

#### `POST /api/posts/{postId}/comments`
- **Request**: `{ "content": "This is a comment" }`
- **Response**: Returns the created comment object (same structure as above item).

---

### **REACTIONS**

#### `POST /api/posts/{postId}/reactions`
React to a post.
- **Request**: `{ "reaction": "LIKE" }` (Options: LIKE, LOVE, HELPFUL, FUNNY)
- **Response**:
```json
{
  "postId": "uuid-string",
  "profileId": "uuid-string",
  "reaction": "LIKE"
}
```

#### `DELETE /api/posts/{postId}/reactions`
Remove your reaction. Returns the simplified reaction object (same as above).

---

### **SOCIAL (Phase 3)**

#### `POST /api/users/{id}/follow`
Follow a user.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `200 OK`

#### `DELETE /api/users/{id}/follow`
Unfollow a user.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `200 OK`

---

### **LANGUAGES**

#### `GET /api/languages`
Returns all available system languages.
- **Response**:
```json
{
  "languages": [
    {
      "id": "uuid",
      "code": "en",
      "name": "English",
      "nativeName": "English",
      "flagEmoji": "🇬🇧"
    },
    {
      "code": "es", ...
    }
  ]
}
```

---

## 5. TypeScript Interfaces

Use these exact types in your frontend:

```typescript
export interface AuthorDto {
  id: string;
  username: string;
  displayName: string;
  avatarUrl?: string;
  language?: string;
  flagEmoji?: string;
  // Stats (added in Phase 3)
  followersCount?: number;
  followingCount?: number;
  postsCount?: number;
}

export interface PostReactionSummary {
  likes: number;
  comments: number;
}

export interface ApiPost {
  id: string;
  content: string;
  originalLanguage: string;
  translation?: string;
  imageUrl?: string;
  
  // Location
  latitude?: number;
  longitude?: number;
  distance?: string; 
  location?: string;

  // Metadata
  author: AuthorDto;
  reactions: PostReactionSummary;
  userReaction?: string | null;
  createdAt: string;
}

export interface ApiComment {
  id: string;
  content: string;
  createdAt: string;
  author: AuthorDto;
}
```
