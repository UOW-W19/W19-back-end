# Contract Compliance Audit & Gap Analysis

**Last Updated:** 2026-01-05  
**Purpose:** Identify gaps between current implementation and API contract

---

## Legend
- ✅ **Complete & Compliant** - Matches contract exactly
- ⚠️ **Implemented but Non-Compliant** - Exists but needs fixes
- ❌ **Missing** - Not implemented yet
- 🔧 **Needs Refactor** - Major changes required

---

## Section 1: Authentication

### Endpoints
| Endpoint | Status | Issues |
|----------|--------|--------|
| `POST /auth/register` | ✅ | None |
| `POST /auth/login` | ✅ | None |
| `POST /auth/refresh` | ✅ | None |
| `POST /auth/logout` | ❌ | Not implemented |

### Response Format
| Field | Contract | Current | Status |
|-------|----------|---------|--------|
| user_id | `user_id` | `userId` | ⚠️ camelCase |
| access_token | `access_token` | `accessToken` | ⚠️ camelCase |
| refresh_token | `refresh_token` | `refreshToken` | ⚠️ camelCase |
| expires_in | `expires_in` | `expiresIn` | ⚠️ camelCase |

**Action Items:**
- [ ] Add `@JsonProperty` annotations for snake_case
- [ ] Implement logout endpoint
- [ ] Test token refresh rotation

---

## Section 2: Users & Profiles

### Endpoints
| Endpoint | Status | Issues |
|----------|--------|--------|
| `GET /users/me` | ✅ | Added in Phase 3.5 |
| `GET /users/{user_id}` | ❌ | Missing public profile endpoint |
| `PATCH /users/me` | ❌ | Missing profile update |
| `GET /users/me/settings` | ⚠️ | Exists, needs snake_case |
| `PATCH /users/me/settings` | ⚠️ | Exists, needs snake_case |
| `POST /users/{user_id}/follow` | ✅ | Implemented |
| `DELETE /users/{user_id}/follow` | ✅ | Implemented |
| `GET /users/{user_id}/followers` | ❌ | Missing |
| `GET /users/{user_id}/following` | ❌ | Missing |
| `POST /users/{user_id}/block` | ✅ | Implemented |
| `DELETE /users/{user_id}/block` | ✅ | Implemented |

### Entities
| Entity | Status | Issues |
|--------|--------|--------|
| Profile | ✅ | Core fields exist |
| UserSettings | ⚠️ | Field names need snake_case |
| NotificationPrefs | ⚠️ | Field names need snake_case |
| PrivacySettings | ⚠️ | Field names need snake_case |
| UserFollow | ✅ | Complete |
| UserBlock | ✅ | Complete |

**Action Items:**
- [ ] Add `GET /users/{user_id}` for public profiles
- [ ] Add `PATCH /users/me` for profile updates
- [ ] Add follower/following list endpoints
- [ ] Fix all field names to snake_case
- [ ] Remove `show_streak` from contract (feature removed)

---

## Section 3: Languages

### Endpoints
| Endpoint | Status | Issues |
|----------|--------|--------|
| `GET /languages` | ✅ | Implemented |
| `GET /users/me/languages` | ❌ | Missing (only PUT exists) |
| `PUT /users/me/languages` | ✅ | Implemented |

### Entities
| Entity | Status | Issues |
|--------|--------|--------|
| Language | ✅ | Complete |
| UserLanguage | ✅ | Complete |

**Action Items:**
- [ ] Add `GET /users/me/languages` endpoint
- [ ] Verify proficiency enum values match contract

---

## Section 4: Posts & Content

### Endpoints
| Endpoint | Status | Issues |
|----------|--------|--------|
| `GET /posts` | ✅ | Feed implemented |
| `GET /posts/{post_id}` | ✅ | Implemented |
| `POST /posts` | ⚠️ | Missing status field (PENDING) |
| `PATCH /posts/{post_id}` | ❌ | Missing update endpoint |
| `DELETE /posts/{post_id}` | ✅ | Implemented |
| `GET /posts/{post_id}/translations` | ❌ | Only POST exists |
| `POST /posts/{post_id}/translations` | ✅ | Implemented |
| `POST /posts/{post_id}/reactions` | ✅ | Implemented |
| `DELETE /posts/{post_id}/reactions/{reaction_type}` | ⚠️ | Path param differs |
| `GET /posts/{post_id}/comments` | ✅ | Implemented |
| `POST /posts/{post_id}/comments` | ✅ | Implemented |
| `DELETE /posts/{post_id}/comments/{comment_id}` | ✅ | Implemented |
| `POST /posts/{post_id}/reports` | ⚠️ | Combined endpoint |
| `POST /comments/{comment_id}/reports` | ❌ | Should be separate |

### Entities
| Entity | Status | Issues |
|--------|--------|--------|
| Post | ⚠️ | Missing `status` field |
| PostReaction | ✅ | Complete |
| PostComment | ⚠️ | Missing `parent_comment_id` |
| PostTranslation | ✅ | Complete |
| PostReport | ⚠️ | Wrong enum values |
| CommentReport | ❌ | Should be separate entity |

### Enum Compliance
| Enum | Contract Values | Current Values | Status |
|------|----------------|----------------|--------|
| ReactionType | LIKE, LOVE, HELPFUL, FUNNY | Same | ✅ |
| ReportReason | SPAM, HARASSMENT, INAPPROPRIATE, MISINFORMATION, OTHER | SPAM, HARASSMENT, HATE_SPEECH, INAPPROPRIATE_CONTENT, OTHER | ❌ |
| PostStatus | PENDING, APPROVED, REJECTED | N/A | ❌ |

**Action Items:**
- [ ] Add `status` field to Post entity (PENDING, APPROVED, REJECTED)
- [ ] Add `PATCH /posts/{post_id}` endpoint
- [ ] Add `GET /posts/{post_id}/translations` endpoint
- [ ] Separate report endpoints: posts vs comments
- [ ] Fix ReportReason enum values
- [ ] Add `parent_comment_id` to PostComment
- [ ] Fix reaction DELETE to use path param
- [ ] Change all field names to snake_case

---

## Section 5: Learning

### Status: ❌ **NOT IMPLEMENTED**

### Missing Endpoints
- [ ] `GET /words` - List saved words
- [ ] `POST /words` - Save new word
- [ ] `GET /words/{word_id}` - Get word details
- [ ] `PATCH /words/{word_id}` - Update word
- [ ] `DELETE /words/{word_id}` - Delete word
- [ ] `POST /practice/sessions` - Start practice
- [ ] `POST /practice/sessions/{session_id}/answer` - Submit answer
- [ ] `POST /practice/sessions/{session_id}/end` - End session
- [ ] `GET /goals` - List goals
- [ ] `POST /goals` - Create goal
- [ ] `PATCH /goals/{goal_id}` - Update goal
- [ ] `DELETE /goals/{goal_id}` - Delete goal

### Missing Entities
- [ ] SavedWord
- [ ] PracticeSession
- [ ] PracticeWord
- [ ] LearningGoal

---

## Section 6: Messaging

### Status: ❌ **NOT IMPLEMENTED**

### Missing Endpoints
- [ ] `GET /conversations`
- [ ] `POST /conversations`
- [ ] `GET /conversations/{conversation_id}/messages`
- [ ] `POST /conversations/{conversation_id}/messages`
- [ ] `PATCH /conversations/{conversation_id}/read`

### Missing Entities
- [ ] Conversation
- [ ] ConversationParticipant
- [ ] Message

### Missing Technology
- [ ] WebSocket support (STOMP/SockJS)

---

## Section 7: Community & Meetups

### Status: ❌ **NOT IMPLEMENTED**

### Missing Endpoints
- [ ] `GET /meetups`
- [ ] `POST /meetups`
- [ ] `GET /meetups/{meetup_id}`
- [ ] `PATCH /meetups/{meetup_id}`
- [ ] `DELETE /meetups/{meetup_id}`
- [ ] `POST /meetups/{meetup_id}/rsvp`
- [ ] `GET /meetups/{meetup_id}/attendees`

### Missing Entities
- [ ] Meetup
- [ ] MeetupAttendee

---

## Section 8: Moderation

### Status: ❌ **NOT IMPLEMENTED**

### Missing Endpoints
- [ ] `GET /admin/reports`
- [ ] `PATCH /admin/reports/{report_id}`
- [ ] `POST /admin/users/{user_id}/ban`
- [ ] `DELETE /admin/users/{user_id}/ban`
- [ ] `PATCH /admin/posts/{post_id}/approve`
- [ ] `PATCH /admin/posts/{post_id}/reject`

---

## Section 9: Notifications

### Status: ❌ **NOT IMPLEMENTED**

### Missing Endpoints
- [ ] `GET /notifications`
- [ ] `PATCH /notifications/{notification_id}/read`
- [ ] `POST /notifications/register-device`

### Missing Entities
- [ ] Notification

---

## Summary Statistics

### Overall Progress
- **Sections Complete:** 0/9 (0%)
- **Sections Partial:** 4/9 (Auth, Users, Languages, Posts)
- **Sections Missing:** 5/9 (Learning, Messaging, Meetups, Moderation, Notifications)

### Endpoint Progress
- **Implemented:** 22 endpoints
- **Needs Fixes:** 8 endpoints
- **Missing:** 40+ endpoints

### Critical Issues by Priority

#### 🔴 **Priority 1: Breaking Changes**
1. **Field Naming** - All responses use camelCase instead of snake_case
2. **Report Endpoints** - Combined instead of separate
3. **Enum Values** - ReportReason doesn't match contract
4. **Missing Status** - Posts don't have PENDING/APPROVED/REJECTED

#### 🟡 **Priority 2: Missing Core Features**
1. **Profile Updates** - No PATCH /users/me
2. **Post Updates** - No PATCH /posts/{post_id}
3. **Follower Lists** - No GET endpoints
4. **Comment Threading** - No parent_comment_id
5. **Translation List** - No GET /posts/{post_id}/translations

#### 🟢 **Priority 3: Future Phases**
1. Learning Core (Section 5)
2. Messaging (Section 6)
3. Meetups (Section 7)
4. Moderation (Section 8)
5. Notifications (Section 9)

---

## Recommended Action Plan

### Phase A: Critical Fixes (1-2 days)
1. Add `@JsonProperty` annotations for snake_case across all DTOs
2. Fix ReportReason enum values
3. Add Post.status field and moderation workflow
4. Separate report endpoints (posts vs comments)

### Phase B: Complete Existing Sections (3-4 days)
1. Add missing user/profile endpoints
2. Add missing post endpoints (PATCH, GET translations)
3. Add comment threading support
4. Fix reaction DELETE endpoint

### Phase C: New Sections (20-30 days)
1. Learning Core
2. Messaging
3. Meetups
4. Moderation
5. Notifications

---

## Next Steps

**Immediate Actions:**
1. Review this audit with stakeholders
2. Prioritize which fixes to tackle first
3. Decide on migration strategy (incremental vs rebuild)
4. Create detailed task breakdown for Phase A

**Questions to Answer:**
- Should we fix existing code or rebuild from scratch?
- Which sections are MVP vs nice-to-have?
- What's the timeline for full contract compliance?
