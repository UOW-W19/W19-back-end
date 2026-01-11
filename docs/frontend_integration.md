# Frontend-Backend Feature Implementation Status

**Last Updated:** 2026-01-10

This document tracks which frontend features have backend API support and which are pending implementation.

---

## ✅ Fully Implemented & Verified (2026-01-10)

| Feature | Frontend | Backend | Endpoint | Status |
|---------|----------|---------|----------|--------|
| **Authentication** | ✅ | ✅ | `POST /api/auth/login` | ✅ Working |
| **User Profile** | ✅ | ✅ | `GET /api/users/me` | ✅ Working |
| **Posts/Feed** | ✅ | ✅ | `GET /api/posts` | ✅ Working |
| **Learning - Words** | ✅ | ✅ | `GET /api/words` | ✅ Working |
| **Learning - Stats** | ✅ | ✅ | `GET /api/learn/stats` | ✅ Working |
| **Nearby Learners** | ✅ | ✅ | `GET /api/learners/nearby` | ✅ Working |
| **Meetups System** | ✅ | ✅ | `POST/GET/PUT/DELETE /api/meetups` | ✅ Working |

---

## 🟡 Frontend Ready, Awaiting Full Testing

| Feature | Frontend | Endpoint | Notes |
|---------|----------|----------|-------|
| **Post Reactions** | ✅ | `POST/DELETE /posts/{id}/reactions` | Need to test like/unlike |
| **Comments** | ✅ | `GET/POST /posts/{id}/comments` | Need to test create/list |
| **Learning Sessions** | ✅ | `POST /api/learn/sessions/start` | Need to add words first |
| **Submit Answer** | ✅ | `POST /api/learn/sessions/{id}/submit` | Requires active session |
| **Complete Session** | ✅ | `POST /api/learn/sessions/{id}/complete` | Requires active session |
| **Session History** | ✅ | `GET /api/learn/sessions` | Need sessions first |
| **User Settings** | ✅ | `GET/PATCH /users/me/settings` | Untested |
| **Languages List** | ✅ | `GET /languages` | Untested |
| **Post Translation** | ✅ | `GET /posts/{id}/translations` | Untested |
| **Reports** | ✅ | `POST /posts/{id}/reports` | Untested |
| **Follow System** | ✅ | `POST/DELETE /users/{id}/follow` | Untested |

---

## ✅ Frontend Service Paths (Updated to Match Backend Contract)

| Service | Frontend Path | Matches Contract |
|---------|---------------|------------------|
| Words CRUD | `/api/words` | ✅ |
| Learning Stats | `/api/learn/stats` | ✅ |
| Start Session | `/api/learn/sessions/start` | ✅ |
| Submit Result | `/api/learn/sessions/{id}/submit` | ✅ |
| Complete Session | `/api/learn/sessions/{id}/complete` | ✅ |
| Session History | `/api/learn/sessions` | ✅ |

---

## ❌ Not Yet Implemented (Backend Needed)

### 2. **AI Object Scanner**
Frontend Location: `src/pages/ScannerPage.tsx`

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/scanner/analyze` | POST | Upload image for AI analysis |

**Request:**
```json
{
  "image": "base64_encoded_image",
  "source_language": "auto",
  "target_language": "en"
}
```

**Response:**
```json
{
  "objects": [
    {
      "label": "apple",
      "translation": "manzana",
      "confidence": 0.95,
      "bounding_box": { "x": 10, "y": 20, "width": 100, "height": 100 }
    }
  ]
}
```

**External APIs Needed:** Google Cloud Vision, Google Translate/DeepL

---

### 3. **Messaging/Chat System**
Frontend Location: `src/pages/MessagesPage.tsx`

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/conversations` | GET | List user's conversations |
| `/api/conversations` | POST | Start new conversation |
| `/api/conversations/{id}` | GET | Get conversation details |
| `/api/conversations/{id}/messages` | GET | Get messages (paginated) |
| `/api/conversations/{id}/messages` | POST | Send message |
| `/api/conversations/{id}/messages/{msgId}/translate` | GET | Translate message |

**WebSocket:** `ws://api/messages` for real-time messaging



### 5. **Notifications**
Frontend: Not yet implemented

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/notifications` | GET | List notifications |
| `/api/notifications/{id}/read` | POST | Mark as read |
| `/api/notifications/read-all` | POST | Mark all as read |

**WebSocket:** `ws://api/notifications` for real-time push

---

### 6. **User Block System**
Frontend: Not yet implemented

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users/{id}/block` | POST | Block a user |
| `/api/users/{id}/block` | DELETE | Unblock a user |

---

## 📋 Implementation Priority Recommendation

### Phase 1 (Core) - ✅ COMPLETED
1. ~~Authentication~~
2. ~~Comments~~
3. ~~Follow/Unfollow~~
4. ~~Learning Sessions~~
5. ~~Reports~~
6. ~~User Settings~~
7. ~~Post Translation~~

### Phase 2 (Discovery) - ✅ COMPLETED
1. ~~Nearby Learners~~
2. ~~Meetups System~~

### Phase 3 (Communication)
1. Messaging System
2. Notifications

### Phase 4 (AI Features)
1. AI Object Scanner

---

## 🔗 API Endpoint Summary

### Authentication (`/api/auth`)
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login and get tokens
- `POST /auth/refresh` - Refresh access token
- `POST /auth/logout` - Logout (client-side)

### Users (`/api/users`)
- `GET /users/me` - Get current user profile
- `PATCH /users/me` - Update profile
- `GET /users/me/settings` - Get settings
- `PATCH /users/me/settings` - Update settings
- `GET /users/{id}` - Get user by ID
- `POST /users/{id}/follow` - Follow user
- `DELETE /users/{id}/follow` - Unfollow user
- `GET /users/{id}/followers` - List followers
- `GET /users/{id}/following` - List following
- `GET /users/me/languages` - Get user languages

### Languages
- `GET /languages` - List all supported languages

### Posts (`/api/posts`)
- `GET /posts` - Get feed (paginated)
- `POST /posts` - Create post
- `GET /posts/{id}` - Get single post
- `DELETE /posts/{id}` - Delete post
- `GET /posts/{id}/translations?target_language=xx` - Get translation
- `POST /posts/{id}/reactions` - Add reaction
- `DELETE /posts/{id}/reactions` - Remove reaction
- `GET /posts/{id}/comments` - List comments
- `POST /posts/{id}/comments` - Add comment
- `DELETE /posts/{id}/comments/{commentId}` - Delete comment
- `POST /posts/{id}/reports` - Report post

### Learning (`/api/words`, `/api/learn`, `/api/stats`)
- `GET /api/words` - List saved words
- `POST /api/words` - Save new word
- `PATCH /api/words/{id}` - Update word
- `DELETE /api/words/{id}` - Delete word
- `POST /api/learn/sessions/start` - Start practice session
- `POST /api/learn/sessions/{id}/submit` - Submit answer
- `POST /api/learn/sessions/{id}/complete` - Complete session
- `GET /api/learn/sessions` - Session history
- `GET /api/stats` - Learning statistics

---

## 🔗 Related Documentation

- [API Contract](./api_contract.md)
- [Database Schema](./database-schema.sql)
- [Learn API Contract](./api_reference.md)
