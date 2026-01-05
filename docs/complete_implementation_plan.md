# Complete Backend Implementation Plan (Contract-Compliant)

## Overview
This plan rebuilds the entire backend to **strictly follow** the API contract at `docs/api_contract.md`. Every endpoint, field name, and response structure will match the contract exactly.

---

## Contract Structure Analysis

Based on `api_contract.md`, the API is organized into these sections:

1. **Authentication** - Register, Login, Token Refresh
2. **Users & Profiles** - Profile management, Settings, Following, Blocking
3. **Languages** - System languages, User language preferences
4. **Posts & Content** - Feed, CRUD, Reactions, Comments, Translations, Reporting
5. **Learning** - Saved Words, Practice Sessions, Goals, AR Scanning
6. **Messaging** - Conversations, Direct Messages, Group Chats
7. **Community & Meetups** - Events, RSVPs, Attendance
8. **Moderation** - Reports, Content Review, User Bans
9. **Notifications** - Activity notifications, Push notifications

---

## Implementation Phases

### Phase 1: Foundation & Authentication
**Goal:** Core infrastructure and user authentication

#### Entities
- `Profile` (users table)
  - Fields: `id`, `username`, `email`, `password_hash`, `display_name`, `avatar_url`, `bio`, `latitude`, `longitude`, `created_at`, `updated_at`
- `RefreshToken`
  - Fields: `id`, `token`, `profile_id`, `expires_at`, `created_at`
- `UserRole`
  - Fields: `id`, `user_id`, `role` (enum: USER, MODERATOR, ADMIN)

#### Endpoints
- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`

#### Verification
- [ ] Registration creates user with hashed password
- [ ] Login returns `access_token`, `refresh_token`, `expires_in`
- [ ] Refresh token rotation works
- [ ] JWT validation on protected routes

---

### Phase 2: Users & Profiles
**Goal:** User profile management and social features

#### Entities
- `UserSettings` (one-to-one with Profile)
  - `notification_prefs` (embedded): `push_enabled`, `email_enabled`, `like_notifications`, `comment_notifications`, `meetup_notifications`
  - `privacy_settings` (embedded): `show_location`, `show_streak`, `allow_messages` (enum: everyone, following, none)
  - `theme`: light, dark, system
- `UserFollow`
  - Fields: `id`, `follower_id`, `following_id`, `created_at`
- `UserBlock`
  - Fields: `id`, `blocker_id`, `blocked_id`, `created_at`

#### Endpoints
- `GET /users/me` - Current user profile
- `GET /users/{user_id}` - Public profile
- `PATCH /users/me` - Update profile
- `GET /users/me/settings` - Get settings
- `PATCH /users/me/settings` - Update settings
- `POST /users/{user_id}/follow` - Follow user
- `DELETE /users/{user_id}/follow` - Unfollow user
- `GET /users/{user_id}/followers` - List followers
- `GET /users/{user_id}/following` - List following
- `POST /users/{user_id}/block` - Block user
- `DELETE /users/{user_id}/block` - Unblock user

#### Field Name Compliance
⚠️ **CRITICAL:** Use snake_case in JSON responses to match contract:
- `display_name` (not `displayName`)
- `avatar_url` (not `avatarUrl`)
- `created_at` (not `createdAt`)
- `notification_prefs` (not `notificationPrefs`)
- `privacy_settings` (not `privacySettings`)

---

### Phase 3: Languages
**Goal:** System languages and user language preferences

#### Entities
- `Language`
  - Fields: `code` (PK), `name`, `native_name`, `flag_emoji`
- `UserLanguage`
  - Fields: `id`, `profile_id`, `language_code`, `proficiency`, `is_learning`
  - Proficiency levels: BEGINNER, INTERMEDIATE, ADVANCED, NATIVE (or A1, A2, B1, B2, C1, C2, NATIVE)

#### Endpoints
- `GET /languages` - List all system languages
- `GET /users/me/languages` - Get user's languages
- `PUT /users/me/languages` - Replace user's language list

#### Data Seeding
- Pre-populate Language table with common languages (en, es, fr, de, ja, zh, etc.)

---

### Phase 4: Posts & Content
**Goal:** Social feed, interactions, and content moderation

#### Entities
- `Post`
  - Fields: `id`, `author_id`, `content`, `original_language`, `latitude`, `longitude`, `image_url`, `status` (PENDING, APPROVED, REJECTED), `created_at`, `updated_at`
- `PostReaction`
  - Fields: `id`, `post_id`, `profile_id`, `reaction` (enum: LIKE, LOVE, HELPFUL, FUNNY), `created_at`
- `PostComment`
  - Fields: `id`, `post_id`, `author_id`, `content`, `parent_comment_id`, `created_at`, `updated_at`
- `PostTranslation`
  - Fields: `id`, `post_id`, `language_code`, `translated_content`, `created_at`
- `PostReport`
  - Fields: `id`, `post_id`, `reporter_id`, `reason` (SPAM, HARASSMENT, INAPPROPRIATE, MISINFORMATION, OTHER), `details`, `status` (PENDING, REVIEWED, RESOLVED), `created_at`
- `CommentReport`
  - Fields: `id`, `comment_id`, `reporter_id`, `reason`, `details`, `status`, `created_at`

#### Endpoints
- `GET /posts` - Feed with pagination, filters
- `GET /posts/{post_id}` - Single post
- `POST /posts` - Create post (returns status: PENDING)
- `PATCH /posts/{post_id}` - Update post
- `DELETE /posts/{post_id}` - Delete post
- `GET /posts/{post_id}/translations` - Get translations
- `POST /posts/{post_id}/translations` - Request translation
- `POST /posts/{post_id}/reactions` - React to post
- `DELETE /posts/{post_id}/reactions/{reaction_type}` - Remove reaction
- `GET /posts/{post_id}/comments` - List comments
- `POST /posts/{post_id}/comments` - Add comment
- `DELETE /posts/{post_id}/comments/{comment_id}` - Delete comment
- `POST /posts/{post_id}/reports` - Report post
- `POST /comments/{comment_id}/reports` - Report comment

#### Key Implementation Notes
- **Separate endpoints** for post reports vs comment reports (not combined)
- **Exact enum values** from contract: SPAM, HARASSMENT, INAPPROPRIATE, MISINFORMATION, OTHER
- **Status field** on posts for moderation workflow
- **Parent comment support** for threaded discussions

---

### Phase 5: Learning Core
**Goal:** Vocabulary management and spaced repetition

#### Entities
- `SavedWord`
  - Fields: `id`, `profile_id`, `word`, `translation`, `language_code`, `source` (POST, AR_SCAN, MANUAL, CHAT), `context`, `mastery_level`, `next_review`, `created_at`
- `PracticeSession`
  - Fields: `id`, `profile_id`, `started_at`, `ended_at`, `words_practiced`, `correct_answers`, `incorrect_answers`
- `PracticeWord`
  - Fields: `id`, `session_id`, `word_id`, `was_correct`, `time_spent`
- `LearningGoal`
  - Fields: `id`, `profile_id`, `goal_type` (WORDS_PER_WEEK, PRACTICE_MINUTES, POSTS_READ), `target_value`, `current_progress`, `deadline`, `created_at`

#### Endpoints
- `GET /words` - List saved words
- `POST /words` - Save new word
- `GET /words/{word_id}` - Get word details
- `PATCH /words/{word_id}` - Update word
- `DELETE /words/{word_id}` - Delete word
- `POST /practice/sessions` - Start practice session
- `POST /practice/sessions/{session_id}/answer` - Submit answer
- `POST /practice/sessions/{session_id}/end` - End session
- `GET /goals` - List goals
- `POST /goals` - Create goal
- `PATCH /goals/{goal_id}` - Update goal
- `DELETE /goals/{goal_id}` - Delete goal

---

### Phase 6: Messaging
**Goal:** Real-time messaging system

#### Entities
- `Conversation`
  - Fields: `id`, `type` (DIRECT, GROUP), `name`, `created_at`, `updated_at`
- `ConversationParticipant`
  - Fields: `id`, `conversation_id`, `profile_id`, `joined_at`, `last_read_at`
- `Message`
  - Fields: `id`, `conversation_id`, `sender_id`, `content`, `read_by`, `created_at`

#### Endpoints
- `GET /conversations` - List conversations
- `POST /conversations` - Create conversation
- `GET /conversations/{conversation_id}/messages` - Get messages
- `POST /conversations/{conversation_id}/messages` - Send message
- `PATCH /conversations/{conversation_id}/read` - Mark as read

#### Technology
- WebSocket support (STOMP/SockJS) for real-time delivery

---

### Phase 7: Community & Meetups
**Goal:** Local events and gatherings

#### Entities
- `Meetup`
  - Fields: `id`, `organizer_id`, `title`, `description`, `location`, `latitude`, `longitude`, `start_time`, `end_time`, `max_attendees`, `created_at`
- `MeetupAttendee`
  - Fields: `id`, `meetup_id`, `profile_id`, `status` (GOING, MAYBE, NOT_GOING), `created_at`

#### Endpoints
- `GET /meetups` - List meetups (with location filtering)
- `POST /meetups` - Create meetup
- `GET /meetups/{meetup_id}` - Get meetup details
- `PATCH /meetups/{meetup_id}` - Update meetup
- `DELETE /meetups/{meetup_id}` - Delete meetup
- `POST /meetups/{meetup_id}/rsvp` - RSVP to meetup
- `GET /meetups/{meetup_id}/attendees` - List attendees

---

### Phase 8: Moderation
**Goal:** Content moderation and user management

#### Endpoints
- `GET /admin/reports` - List all reports (admin only)
- `PATCH /admin/reports/{report_id}` - Review report
- `POST /admin/users/{user_id}/ban` - Ban user
- `DELETE /admin/users/{user_id}/ban` - Unban user
- `PATCH /admin/posts/{post_id}/approve` - Approve post
- `PATCH /admin/posts/{post_id}/reject` - Reject post

---

### Phase 9: Notifications
**Goal:** Activity notifications and push notifications

#### Entities
- `Notification`
  - Fields: `id`, `recipient_id`, `type`, `actor_id`, `entity_type`, `entity_id`, `message`, `read`, `created_at`

#### Endpoints
- `GET /notifications` - List notifications
- `PATCH /notifications/{notification_id}/read` - Mark as read
- `POST /notifications/register-device` - Register push token

---

## Critical Compliance Checklist

### JSON Response Format
- [ ] All field names use `snake_case` (not camelCase)
- [ ] Dates formatted as ISO 8601 strings
- [ ] UUIDs as strings, not objects
- [ ] Pagination follows contract structure

### Enum Values
- [ ] Reaction types: LIKE, LOVE, HELPFUL, FUNNY
- [ ] Report reasons: SPAM, HARASSMENT, INAPPROPRIATE, MISINFORMATION, OTHER
- [ ] Post status: PENDING, APPROVED, REJECTED
- [ ] Message privacy: everyone, following, none

### Endpoint Paths
- [ ] Exact paths from contract (e.g., `/posts/{post_id}/reports` not `/api/posts/{postId}/reports`)
- [ ] Separate endpoints for post vs comment reports
- [ ] Correct HTTP methods (POST, GET, PATCH, DELETE)

### Response Structures
- [ ] Profile includes all fields from contract
- [ ] Settings structure matches exactly
- [ ] Error responses follow contract format

---

## Migration Strategy

### Option 1: Clean Rebuild
1. Create new branch `feature/contract-compliant`
2. Keep existing code as reference
3. Rebuild section by section
4. Test against contract after each phase

### Option 2: Incremental Refactor
1. Create mapping document (current → contract)
2. Add @JsonProperty annotations for field names
3. Refactor endpoints one section at a time
4. Maintain backward compatibility during transition

**Recommendation:** Option 1 (Clean Rebuild) for maximum contract compliance

---

## Testing Strategy

### Contract Validation
- [ ] Create test suite that validates every endpoint against contract
- [ ] JSON schema validation for all responses
- [ ] Field name validation (snake_case check)
- [ ] Enum value validation

### Integration Tests
- [ ] End-to-end user flows
- [ ] Cross-section dependencies (e.g., posts → translations)
- [ ] Permission checks (user vs admin endpoints)

---

## Timeline Estimate

- **Phase 1 (Auth):** 2-3 days
- **Phase 2 (Users):** 3-4 days
- **Phase 3 (Languages):** 1-2 days
- **Phase 4 (Posts):** 5-7 days
- **Phase 5 (Learning):** 4-5 days
- **Phase 6 (Messaging):** 5-6 days
- **Phase 7 (Meetups):** 2-3 days
- **Phase 8 (Moderation):** 2-3 days
- **Phase 9 (Notifications):** 3-4 days

**Total:** 27-37 days for complete implementation

---

## Next Steps

1. **Review this plan** - Confirm approach and priorities
2. **Choose migration strategy** - Clean rebuild vs incremental
3. **Set phase order** - Which sections to implement first
4. **Create detailed task breakdown** - Granular checklist for Phase 1

Would you like me to proceed with Phase 1 implementation, or would you prefer to adjust the plan first?
