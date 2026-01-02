# API Reference

This document lists the available endpoints for the W19 Backend.

## Authentication (`/api/auth`)

### Register User
- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Auth Required**: No
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "username": "username",
    "password": "strongpassword",
    "displayName": "Full Name"
  }
  ```
- **Success Response (201 Created)**: Returns an `AuthResponse` with user ID and JWT.

### Login
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Auth Required**: No
- **Request Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password"
  }
  ```
- **Success Response (200 OK)**: Returns an `AuthResponse` with user ID and JWT.

---

## Profiles (`/api/profiles`)

### Get My Profile
- **URL**: `/api/profiles/me`
- **Method**: `GET`
- **Auth Required**: **Yes** (Bearer Token)
- **Success Response (200 OK)**:
  ```json
  {
    "id": 1,
    "username": "username",
    "email": "user@example.com",
    "displayName": "Full Name",
    "avatarUrl": null,
    "bio": null,
    "latitude": null,
    "longitude": null,
    "roles": ["USER"]
  }
  ```
- **Error Response (403 Forbidden)**: Missing or invalid JWT.
