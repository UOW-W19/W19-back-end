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

## 4. Implementation Snippet (Axios)

```javascript
import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8080/api' });

// Add token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Example: Create a Post
export const createPost = async (content) => {
  return api.post('/posts', { content, originalLanguage: 'en' });
};
```
