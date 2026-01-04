# Locale API Documentation

Complete REST API specification for the Locale language learning app.

**Base URL:** `https://api.locale-app.com/v1`  
**Authentication:** Bearer Token (JWT)  
**Content-Type:** `application/json`

---

## Table of Contents

1. [Authentication](#1-authentication)
2. [Users & Profiles](#2-users--profiles)
3. [Languages](#3-languages)
4. [Posts & Content](#4-posts--content)
5. [Learning](#5-learning)
6. [Messaging](#6-messaging)
7. [Community & Meetups](#7-community--meetups)
8. [Moderation](#8-moderation)
9. [Notifications](#9-notifications)

---

## 1. Authentication

### Register
```http
POST /auth/register
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
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

### Login
```http
POST /auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response:** `200 OK`
```json
{
  "user_id": "uuid",
  "access_token": "jwt_token",
  "refresh_token": "refresh_token",
  "expires_in": 3600
}
```

---

### Refresh Token
```http
POST /auth/refresh
```

**Request Body:**
```json
{
  "refresh_token": "refresh_token"
}
```

---

### Logout
```http
POST /auth/logout
Authorization: Bearer {token}
```

---

## 2. Users & Profiles

### Get Current User Profile
```http
GET /users/me
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "username": "johndoe",
  "display_name": "John Doe",
  "avatar_url": "https://...",
  "bio": "Language enthusiast",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "languages": [
    {
      "code": "en",
      "proficiency": "NATIVE",
      "is_learning": false
    },
    {
      "code": "es",
      "proficiency": "B2",
      "is_learning": true
    }
  ],
  "streak": {
    "current": 15,
    "longest": 30
  },
  "trust_score": 95,
  "created_at": "2024-01-15T10:30:00Z"
}
```

---

### Get User Profile by ID
```http
GET /users/{user_id}
Authorization: Bearer {token}
```

---

### Update Profile
```http
PATCH /users/me
Authorization: Bearer {token}
```

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

---

### Update User Languages
```http
PUT /users/me/languages
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "languages": [
    {
      "language_code": "en",
      "proficiency": "NATIVE",
      "is_learning": false
    },
    {
      "language_code": "ja",
      "proficiency": "A2",
      "is_learning": true
    }
  ]
}
```

---

### Get User Settings
```http
GET /users/me/settings
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "notification_prefs": {
    "push_enabled": true,
    "email_enabled": false,
    "like_notifications": true,
    "comment_notifications": true,
    "meetup_notifications": true
  },
  "privacy_settings": {
    "show_location": true,
    "show_streak": true,
    "allow_messages": "everyone"
  },
  "theme": "system"
}
```

---

### Update User Settings
```http
PATCH /users/me/settings
Authorization: Bearer {token}
```

---

### Follow User
```http
POST /users/{user_id}/follow
Authorization: Bearer {token}
```

---

### Unfollow User
```http
DELETE /users/{user_id}/follow
Authorization: Bearer {token}
```

---

### Get Followers
```http
GET /users/{user_id}/followers
Authorization: Bearer {token}
```

**Query Parameters:**
- `page` (default: 1)
- `limit` (default: 20)

---

### Get Following
```http
GET /users/{user_id}/following
Authorization: Bearer {token}
```

---

### Block User
```http
POST /users/{user_id}/block
Authorization: Bearer {token}
```

---

### Unblock User
```http
DELETE /users/{user_id}/block
Authorization: Bearer {token}
```

---

## 3. Languages

### Get All Languages
```http
GET /languages
```

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

## 4. Posts & Content

### Get Feed (Distance-Sorted)
```http
GET /posts/feed
Authorization: Bearer {token}
```

**Query Parameters:**
- `latitude` (required): User's current latitude
- `longitude` (required): User's current longitude
- `radius_km` (default: 50): Search radius in kilometers
- `language` (optional): Filter by language code
- `page` (default: 1)
- `limit` (default: 20)

**Response:** `200 OK`
```json
{
  "posts": [
    {
      "id": "uuid",
      "author": {
        "id": "uuid",
        "username": "maria_es",
        "display_name": "Maria",
        "avatar_url": "https://..."
      },
      "content": "¡Hola amigos! Hoy visité un café nuevo.",
      "original_language": "es",
      "translations": {
        "en": "Hello friends! Today I visited a new café."
      },
      "image_url": "https://...",
      "latitude": 40.4168,
      "longitude": -3.7038,
      "distance_km": 2.5,
      "reactions_count": {
        "LIKE": 15,
        "LOVE": 3,
        "HELPFUL": 8
      },
      "user_reaction": "LIKE",
      "comments_count": 5,
      "created_at": "2024-01-20T14:30:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 150,
    "has_more": true
  }
}
```

---

### Get Post by ID
```http
GET /posts/{post_id}
Authorization: Bearer {token}
```

---

### Create Post
```http
POST /posts
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "content": "Learning Japanese in Tokyo!",
  "original_language": "en",
  "latitude": 35.6762,
  "longitude": 139.6503,
  "image_url": "https://..."
}
```

**Response:** `201 Created`
```json
{
  "id": "uuid",
  "status": "PENDING",
  "message": "Post submitted for review"
}
```

---

### Update Post
```http
PATCH /posts/{post_id}
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "content": "Updated content"
}
```

---

### Delete Post
```http
DELETE /posts/{post_id}
Authorization: Bearer {token}
```

---

### Get Post Translations
```http
GET /posts/{post_id}/translations
Authorization: Bearer {token}
```

---

### Request Translation
```http
POST /posts/{post_id}/translations
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "target_language": "ja"
}
```

---

### React to Post
```http
POST /posts/{post_id}/reactions
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reaction": "LIKE"
}
```

**Reaction Types:** `LIKE`, `LOVE`, `HELPFUL`, `FUNNY`

---

### Remove Reaction
```http
DELETE /posts/{post_id}/reactions/{reaction_type}
Authorization: Bearer {token}
```

---

### Get Post Comments
```http
GET /posts/{post_id}/comments
Authorization: Bearer {token}
```

**Query Parameters:**
- `page` (default: 1)
- `limit` (default: 20)

---

### Add Comment
```http
POST /posts/{post_id}/comments
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "content": "Great post!",
  "parent_comment_id": null
}
```

---

### Delete Comment
```http
DELETE /posts/{post_id}/comments/{comment_id}
Authorization: Bearer {token}
```

---

### Report Post
```http
POST /posts/{post_id}/reports
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "SPAM",
  "details": "This is promotional content"
}
```

**Report Reasons:** `SPAM`, `HARASSMENT`, `INAPPROPRIATE`, `MISINFORMATION`, `OTHER`

---

### Report Comment
```http
POST /comments/{comment_id}/reports
Authorization: Bearer {token}
```

---

## 5. Learning

### Saved Words

#### Get Saved Words
```http
GET /words
Authorization: Bearer {token}
```

**Query Parameters:**
- `language` (optional): Filter by language
- `source` (optional): Filter by source (`POST`, `AR_SCAN`, `MANUAL`, `CHAT`)
- `sort` (default: `created_at`): `created_at`, `next_review`, `mastery_level`
- `page` (default: 1)
- `limit` (default: 50)

**Response:** `200 OK`
```json
{
  "words": [
    {
      "id": "uuid",
      "word": "おはよう",
      "translation": "Good morning",
      "language_code": "ja",
      "source": "AR_SCAN",
      "context": "Sign at Tokyo station",
      "mastery_level": 3,
      "next_review": "2024-01-22T10:00:00Z",
      "created_at": "2024-01-15T08:30:00Z"
    }
  ],
  "total": 150
}
```

---

#### Save Word
```http
POST /words
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "word": "bonjour",
  "translation": "hello",
  "language_code": "fr",
  "source": "MANUAL",
  "source_id": null,
  "context": "Common greeting"
}
```

---

#### Update Word
```http
PATCH /words/{word_id}
Authorization: Bearer {token}
```

---

#### Delete Word
```http
DELETE /words/{word_id}
Authorization: Bearer {token}
```

---

### AR Scanning

#### Submit AR Scan
```http
POST /scans
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

**Form Data:**
- `image`: Image file
- `latitude`: Decimal
- `longitude`: Decimal

**Response:** `202 Accepted`
```json
{
  "scan_id": "uuid",
  "status": "PENDING",
  "message": "Image submitted for processing"
}
```

---

#### Get Scan Result
```http
GET /scans/{scan_id}
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "status": "PROCESSED",
  "detected_text": "いらっしゃいませ",
  "detected_language": "ja",
  "translation": "Welcome",
  "words": [
    {
      "word": "いらっしゃいませ",
      "translation": "Welcome (formal)",
      "saved": false
    }
  ],
  "created_at": "2024-01-20T15:00:00Z"
}
```

---

#### Get User's Scans
```http
GET /scans
Authorization: Bearer {token}
```

---

### Practice

#### Start Practice Session
```http
POST /practice/sessions
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "word_count": 10,
  "language": "ja"
}
```

**Response:** `201 Created`
```json
{
  "session_id": "uuid",
  "words": [
    {
      "id": "uuid",
      "word": "おはよう",
      "options": ["Good morning", "Good evening", "Goodbye", "Hello"],
      "source": "AR_SCAN",
      "context": "Sign at Tokyo station"
    }
  ]
}
```

---

#### Submit Practice Answer
```http
POST /practice/sessions/{session_id}/answers
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "word_id": "uuid",
  "answer": "Good morning",
  "response_time_ms": 2500
}
```

**Response:** `200 OK`
```json
{
  "is_correct": true,
  "correct_answer": "Good morning",
  "new_mastery_level": 4
}
```

---

#### End Practice Session
```http
POST /practice/sessions/{session_id}/end
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "session_id": "uuid",
  "words_practiced": 10,
  "correct_count": 8,
  "accuracy": 0.8
}
```

---

#### Get Practice History
```http
GET /practice/sessions
Authorization: Bearer {token}
```

---

### Goals

#### Get User Goals
```http
GET /goals
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "goals": [
    {
      "id": "uuid",
      "goal": "WORDS_LEARNED",
      "target": 100,
      "progress": 67,
      "deadline": "2024-02-01",
      "completed": false
    }
  ]
}
```

---

#### Create Goal
```http
POST /goals
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "goal": "WORDS_LEARNED",
  "target": 50,
  "deadline": "2024-02-15"
}
```

**Goal Types:** `WORDS_LEARNED`, `POSTS_READ`, `PRACTICE_MINS`

---

#### Update Goal
```http
PATCH /goals/{goal_id}
Authorization: Bearer {token}
```

---

#### Delete Goal
```http
DELETE /goals/{goal_id}
Authorization: Bearer {token}
```

---

---

## 6. Messaging

### Conversations

#### Get Conversations
```http
GET /conversations
Authorization: Bearer {token}
```

**Query Parameters:**
- `type` (optional): `DIRECT`, `GROUP`
- `archived` (default: false)

**Response:** `200 OK`
```json
{
  "conversations": [
    {
      "id": "uuid",
      "type": "DIRECT",
      "title": null,
      "participants": [
        {
          "id": "uuid",
          "username": "maria_es",
          "display_name": "Maria",
          "avatar_url": "https://..."
        }
      ],
      "last_message": {
        "content": "See you tomorrow!",
        "sender_id": "uuid",
        "created_at": "2024-01-20T18:30:00Z"
      },
      "unread_count": 2,
      "is_muted": false,
      "updated_at": "2024-01-20T18:30:00Z"
    }
  ]
}
```

---

#### Create Direct Conversation
```http
POST /conversations/direct
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "user_id": "uuid"
}
```

---

#### Create Group Conversation
```http
POST /conversations/group
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "title": "Tokyo Language Exchange",
  "image_url": "https://...",
  "member_ids": ["uuid1", "uuid2", "uuid3"]
}
```

---

#### Get Conversation Details
```http
GET /conversations/{conversation_id}
Authorization: Bearer {token}
```

---

#### Update Group Conversation
```http
PATCH /conversations/{conversation_id}
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "title": "New Group Name",
  "image_url": "https://..."
}
```

---

#### Add Group Members
```http
POST /conversations/{conversation_id}/members
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "user_ids": ["uuid1", "uuid2"]
}
```

---

#### Remove Group Member
```http
DELETE /conversations/{conversation_id}/members/{user_id}
Authorization: Bearer {token}
```

---

#### Leave Conversation
```http
POST /conversations/{conversation_id}/leave
Authorization: Bearer {token}
```

---

#### Archive Conversation
```http
POST /conversations/{conversation_id}/archive
Authorization: Bearer {token}
```

---

#### Mute/Unmute Conversation
```http
POST /conversations/{conversation_id}/mute
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "muted": true
}
```

---

### Messages

#### Get Messages
```http
GET /conversations/{conversation_id}/messages
Authorization: Bearer {token}
```

**Query Parameters:**
- `before` (optional): Cursor for pagination (message ID)
- `limit` (default: 50)

**Response:** `200 OK`
```json
{
  "messages": [
    {
      "id": "uuid",
      "sender": {
        "id": "uuid",
        "username": "johndoe",
        "avatar_url": "https://..."
      },
      "content": "Hola! ¿Cómo estás?",
      "type": "TEXT",
      "translations": {
        "en": "Hello! How are you?"
      },
      "reply_to": null,
      "created_at": "2024-01-20T14:30:00Z"
    }
  ],
  "has_more": true
}
```

---

#### Send Text Message
```http
POST /conversations/{conversation_id}/messages
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "content": "Hello!",
  "type": "TEXT",
  "reply_to_id": null
}
```

---

#### Send Voice Message
```http
POST /conversations/{conversation_id}/messages/voice
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

**Form Data:**
- `audio`: Audio file
- `reply_to_id` (optional): UUID

---

#### Send Image Message
```http
POST /conversations/{conversation_id}/messages/image
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

**Form Data:**
- `image`: Image file
- `caption` (optional): Text
- `reply_to_id` (optional): UUID

---

#### Delete Message
```http
DELETE /conversations/{conversation_id}/messages/{message_id}
Authorization: Bearer {token}
```

---

#### Get Message Translation
```http
GET /messages/{message_id}/translations/{language_code}
Authorization: Bearer {token}
```

---

#### Mark Conversation as Read
```http
POST /conversations/{conversation_id}/read
Authorization: Bearer {token}
```

---

## 7. Community & Meetups

### Get Nearby Meetups
```http
GET /meetups
Authorization: Bearer {token}
```

**Query Parameters:**
- `latitude` (required)
- `longitude` (required)
- `radius_km` (default: 25)
- `language` (optional)
- `status` (default: `PUBLISHED`)
- `from_date` (optional)
- `to_date` (optional)
- `page` (default: 1)
- `limit` (default: 20)

**Response:** `200 OK`
```json
{
  "meetups": [
    {
      "id": "uuid",
      "organizer": {
        "id": "uuid",
        "username": "tanaka_jp",
        "display_name": "Tanaka",
        "avatar_url": "https://..."
      },
      "title": "Japanese Conversation Practice",
      "description": "Casual meetup for Japanese learners",
      "location_name": "Shibuya Cafe",
      "latitude": 35.6595,
      "longitude": 139.7004,
      "distance_km": 1.2,
      "scheduled_at": "2024-01-25T18:00:00Z",
      "language": {
        "code": "ja",
        "name": "Japanese",
        "flag_emoji": "🇯🇵"
      },
      "max_participants": 10,
      "participants_count": 6,
      "user_status": null,
      "status": "PUBLISHED"
    }
  ]
}
```

---

### Get Meetup Details
```http
GET /meetups/{meetup_id}
Authorization: Bearer {token}
```

---

### Create Meetup
```http
POST /meetups
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "title": "Spanish Conversation Night",
  "description": "Practice Spanish with native speakers",
  "location_name": "Downtown Coffee Shop",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "scheduled_at": "2024-02-01T19:00:00Z",
  "language_code": "es",
  "max_participants": 8
}
```

---

### Update Meetup
```http
PATCH /meetups/{meetup_id}
Authorization: Bearer {token}
```

---

### Publish Meetup
```http
POST /meetups/{meetup_id}/publish
Authorization: Bearer {token}
```

---

### Cancel Meetup
```http
POST /meetups/{meetup_id}/cancel
Authorization: Bearer {token}
```

---

### Join Meetup
```http
POST /meetups/{meetup_id}/join
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "status": "PENDING",
  "message": "Join request submitted"
}
```

---

### Leave Meetup
```http
DELETE /meetups/{meetup_id}/join
Authorization: Bearer {token}
```

---

### Get Meetup Participants
```http
GET /meetups/{meetup_id}/participants
Authorization: Bearer {token}
```

---

### Update Participant Status (Organizer Only)
```http
PATCH /meetups/{meetup_id}/participants/{user_id}
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "status": "CONFIRMED"
}
```

**Statuses:** `PENDING`, `CONFIRMED`, `DECLINED`, `ATTENDED`

---

### Get User's Meetups
```http
GET /users/me/meetups
Authorization: Bearer {token}
```

**Query Parameters:**
- `role` (optional): `organizer`, `participant`
- `status` (optional): `upcoming`, `past`

---

## 8. Moderation

*Requires `moderator` or `admin` role*

### Get Pending Posts
```http
GET /moderation/posts
Authorization: Bearer {token}
```

**Query Parameters:**
- `status` (default: `PENDING`): `PENDING`, `FLAGGED`
- `page` (default: 1)
- `limit` (default: 20)

---

### Approve Post
```http
POST /moderation/posts/{post_id}/approve
Authorization: Bearer {token}
```

---

### Reject Post
```http
POST /moderation/posts/{post_id}/reject
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Content violates community guidelines"
}
```

---

### Remove Post
```http
POST /moderation/posts/{post_id}/remove
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Spam content",
  "notify_user": true
}
```

---

### Get Reports
```http
GET /moderation/reports
Authorization: Bearer {token}
```

**Query Parameters:**
- `type` (optional): `post`, `comment`
- `status` (default: `OPEN`)
- `page` (default: 1)
- `limit` (default: 20)

---

### Handle Report
```http
POST /moderation/reports/{report_id}/resolve
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "action": "REMOVE",
  "notes": "Confirmed spam"
}
```

---

### Get Translation Flags
```http
GET /moderation/translations
Authorization: Bearer {token}
```

---

### Correct Translation
```http
POST /moderation/translations/{flag_id}/correct
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "corrected_text": "Corrected translation here"
}
```

---

### Dismiss Translation Flag
```http
POST /moderation/translations/{flag_id}/dismiss
Authorization: Bearer {token}
```

---

### Get Users
```http
GET /moderation/users
Authorization: Bearer {token}
```

**Query Parameters:**
- `search` (optional): Username search
- `sanctioned` (optional): Filter sanctioned users
- `page` (default: 1)
- `limit` (default: 20)

---

### Warn User
```http
POST /moderation/users/{user_id}/warn
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Inappropriate behavior"
}
```

---

### Mute User
```http
POST /moderation/users/{user_id}/mute
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Spam posting",
  "duration_hours": 24
}
```

---

### Suspend User
```http
POST /moderation/users/{user_id}/suspend
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Repeated violations",
  "duration_days": 7
}
```

---

### Ban User
```http
POST /moderation/users/{user_id}/ban
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "reason": "Severe policy violation",
  "permanent": true
}
```

---

### Unban User
```http
POST /moderation/users/{user_id}/unban
Authorization: Bearer {token}
```

---

### Get Moderation Log
```http
GET /moderation/log
Authorization: Bearer {token}
```

**Query Parameters:**
- `actor_id` (optional)
- `target_type` (optional)
- `action` (optional)
- `from_date` (optional)
- `to_date` (optional)
- `page` (default: 1)
- `limit` (default: 50)

---

### Get Appeals
```http
GET /moderation/appeals
Authorization: Bearer {token}
```

**Query Parameters:**
- `status` (default: `PENDING`)

---

### Handle Appeal
```http
POST /moderation/appeals/{appeal_id}/resolve
Authorization: Bearer {token}
```

**Request Body:**
```json
{
  "decision": "APPROVED",
  "notes": "Appeal accepted, sanction removed"
}
```

---

## 9. Notifications

### Get Notifications
```http
GET /notifications
Authorization: Bearer {token}
```

**Query Parameters:**
- `unread_only` (default: false)
- `type` (optional): `LIKE`, `COMMENT`, `FOLLOW`, `MENTION`, `MEETUP`, `MODERATION`, `SYSTEM`
- `page` (default: 1)
- `limit` (default: 30)

**Response:** `200 OK`
```json
{
  "notifications": [
    {
      "id": "uuid",
      "type": "LIKE",
      "title": "New reaction",
      "body": "Maria liked your post",
      "data": {
        "post_id": "uuid",
        "user_id": "uuid"
      },
      "read_at": null,
      "created_at": "2024-01-20T15:30:00Z"
    }
  ],
  "unread_count": 5
}
```

---

### Mark Notification as Read
```http
POST /notifications/{notification_id}/read
Authorization: Bearer {token}
```

---

### Mark All as Read
```http
POST /notifications/read-all
Authorization: Bearer {token}
```

---

### Get Unread Count
```http
GET /notifications/unread-count
Authorization: Bearer {token}
```

**Response:** `200 OK`
```json
{
  "count": 5
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "error": "BAD_REQUEST",
  "message": "Invalid request body",
  "details": {
    "field": "email",
    "issue": "Invalid email format"
  }
}
```

### 401 Unauthorized
```json
{
  "error": "UNAUTHORIZED",
  "message": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
  "error": "FORBIDDEN",
  "message": "You don't have permission to perform this action"
}
```

### 404 Not Found
```json
{
  "error": "NOT_FOUND",
  "message": "Resource not found"
}
```

### 409 Conflict
```json
{
  "error": "CONFLICT",
  "message": "Resource already exists"
}
```

### 422 Unprocessable Entity
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": [
    {
      "field": "username",
      "issue": "Username already taken"
    }
  ]
}
```

### 429 Too Many Requests
```json
{
  "error": "RATE_LIMITED",
  "message": "Too many requests",
  "retry_after": 60
}
```

### 500 Internal Server Error
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred"
}
```

---

## WebSocket Events

### Connection
```
wss://api.locale-app.com/ws?token={jwt_token}
```

### Events

#### New Message
```json
{
  "event": "message.new",
  "data": {
    "conversation_id": "uuid",
    "message": { ... }
  }
}
```

#### Message Deleted
```json
{
  "event": "message.deleted",
  "data": {
    "conversation_id": "uuid",
    "message_id": "uuid"
  }
}
```

#### Typing Indicator
```json
{
  "event": "typing.start",
  "data": {
    "conversation_id": "uuid",
    "user_id": "uuid"
  }
}
```

#### New Notification
```json
{
  "event": "notification.new",
  "data": {
    "notification": { ... }
  }
}
```

---

## Rate Limits

| Endpoint Category | Limit |
|-------------------|-------|
| Authentication | 10 req/min |
| Posts (read) | 100 req/min |
| Posts (write) | 20 req/min |
| Messages | 60 req/min |
| AR Scans | 10 req/min |
| General | 200 req/min |

---

*Last updated: December 2024*
