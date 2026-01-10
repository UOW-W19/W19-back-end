# Locale API Documentation

**Base URL:** `http://localhost:8081/api`  
**Authentication:** Bearer Token (JWT)  
**Format:** JSON (`snake_case`)

---

## 1. Authentication
*   `POST /auth/register`
*   `POST /auth/login`
*   `POST /auth/refresh`
*   `POST /auth/logout`

## 2. Users & Profiles
*   `GET /users/me`
*   `PATCH /users/me`
*   `GET /users/me/settings`
*   `PATCH /users/me/settings`
*   `GET /users/{id}`
*   `POST /users/{id}/follow`
*   `DELETE /users/{id}/follow`
*   `GET /users/me/languages`

## 3. Languages
*   `GET /languages`

## 4. Posts & Content
*   `GET /posts` (Feed)
*   `POST /posts`
*   `GET /posts/{id}`
*   `DELETE /posts/{id}`
*   `GET /posts/{id}/translations?target_language=xx`
*   `POST /posts/{id}/reactions`
*   `DELETE /posts/{id}/reactions`
*   `POST /posts/{id}/comments`
*   `GET /posts/{id}/comments`
*   `POST /posts/{id}/reports`

## 5. Learning Core (`/api/practice` & `/api/words`)

### Vocabulary
*   `GET /api/words`
*   `POST /api/words`
*   `PATCH /api/words/{id}`
*   `DELETE /api/words/{id}`

### Practice Sessions
*   `POST /api/practice/sessions` - Start Session
*   `POST /api/practice/sessions/{id}/results` - Submit Word Result
*   `POST /api/practice/sessions/{id}/complete` - Finish Session
*   `GET /api/practice/sessions` - Session History

### Stats
*   `GET /api/stats`

## 6. Pending / Future
*   Messaging / Chat
*   Meetups
*   Notifications
