# Frontend Integration Guide (JWT)

This guide explains how to connect your frontend application to the W19 Backend's JWT-based authentication system.

## 1. Authentication Flow

### Registration & Login
When a user registers or logs in, the backend returns a JSON response containing an `accessToken`.

**Endpoint**: `POST /api/auth/login` or `POST /api/auth/register`
**Response**:
```json
{
  "userId": 1,
  "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## 2. Token Management

### Storing the Token
You should store the `accessToken` securely on the client side. Common options:
- **`localStorage`**: Easy to use, persistent across sessions.
- **`sessionStorage`**: Only lasts for the current tab session.
- **`HttpOnly` Cookie**: (Requires backend adjustment to set cookies instead of JSON response).

### Sending the Token
For every request to a **protected endpoint** (like `/api/profiles/me`), you must include the token in the `Authorization` header.

**Header Format**:
`Authorization: Bearer <your_token_here>`

## 3. JavaScript Example (using Fetch)

```javascript
const API_URL = "http://localhost:8080/api";

// 1. Login to get token
async function login(email, password) {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  });
  
  const data = await response.json();
  if (response.ok) {
    localStorage.setItem("token", data.accessToken);
    console.log("Logged in successfully!");
  }
}

// 2. Fetch protected data
async function getMyProfile() {
  const token = localStorage.getItem("token");
  
  const response = await fetch(`${API_URL}/profiles/me`, {
    headers: {
      "Authorization": `Bearer ${token}`
    }
  });
  
  if (response.ok) {
    const profile = await response.json();
    console.log("User Profile:", profile);
  } else if (response.status === 403) {
    console.error("Session expired or unauthorized");
  }
}
```

## 4. Handling Expiration
The token has a limited lifespan (configured in `application.yml`). If a request returns `403 Forbidden` (or a custom error), the frontend should redirect the user to the login page to obtain a new token.
