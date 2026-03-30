# Frontend Integration Guide

## API Base URL

- Development: `http://localhost:8080/api`
- Staging: `https://staging-api.example.com/api`
- Production: `https://api.example.com/api`

## Authentication Flow

### Google OAuth Login

1. User clicks "Sign in with Google" button
2. Frontend obtains Google ID token from Google Sign-In
3. Send token to backend:

```javascript
POST /api/auth/google
Content-Type: application/json

{
  "idToken": "google-id-token-here"
}
```

4. Backend response:

```javascript
{
  "timestamp": "2024-01-15T10:30:00",
  "success": true,
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "email": "user@example.com",
    "fullName": "John Doe",
    "userId": 1,
    "avatarUrl": "https://...",
    "expiresIn": 900000
  }
}
```

5. Store tokens securely (use secure storage, not localStorage for sensitive apps)
6. Include access token in all subsequent requests

### Making Authenticated Requests

```javascript
GET /api/users/me
Authorization: Bearer {accessToken}
```

### Token Refresh

When access token expires (15 minutes):

```javascript
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

Response contains new access and refresh tokens.

### Logout

```javascript
POST /api/auth/logout
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

## Standard Response Format

All API responses follow this structure:

### Success Response

```javascript
{
  "timestamp": "2024-01-15T10:30:00",
  "success": true,
  "message": "Optional success message",
  "data": { /* actual response data */ },
  "error": null
}
```

### Error Response

```javascript
{
  "timestamp": "2024-01-15T10:30:00",
  "success": false,
  "message": null,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human-readable error message",
    "detail": "Optional additional details"
  }
}
```

## Error Handling

### Common Error Codes

| Code               | HTTP Status | Meaning                   | Action             |
| ------------------ | ----------- | ------------------------- | ------------------ |
| `TOKEN_EXPIRED`    | 401         | Access token expired      | Refresh token      |
| `INVALID_TOKEN`    | 401         | Token invalid/blacklisted | Re-authenticate    |
| `UNAUTHORIZED`     | 401         | Not authenticated         | Redirect to login  |
| `FORBIDDEN`        | 403         | Insufficient permissions  | Show error message |
| `NOT_FOUND`        | 404         | Resource not found        | Show not found UI  |
| `VALIDATION_ERROR` | 400         | Input validation failed   | Show field errors  |
| `INTERNAL_ERROR`   | 500         | Server error              | Show generic error |

### Frontend Error Handling Pattern

```javascript
async function apiCall(endpoint, options) {
  try {
    const response = await fetch(endpoint, options);
    const data = await response.json();

    if (!data.success) {
      // Handle API error
      switch (data.error.code) {
        case 'TOKEN_EXPIRED':
          await refreshToken();
          return apiCall(endpoint, options); // Retry
        case 'UNAUTHORIZED':
          redirectToLogin();
          break;
        case 'VALIDATION_ERROR':
          showValidationErrors(data.error.message);
          break;
        default:
          showError(data.error.message);
      }
      throw new Error(data.error.message);
    }

    return data.data;
  } catch (error) {
    console.error('API call failed:', error);
    throw error;
  }
}
```

## API Endpoints Reference

### Authentication

- `POST /api/auth/google` - Login with Google
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - Logout user
- `GET /api/auth/me` - Get current user info

### Users

- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `PUT /api/users/{id}` - Update user profile
- `GET /api/users/{id}/stats` - Get user statistics

### Accounts

- `GET /api/accounts` - List user's accounts
- `GET /api/accounts/{id}` - Get account details
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Delete account

### Transactions

- `GET /api/transactions` - List transactions
- `GET /api/transactions/{id}` - Get transaction details
- `POST /api/transactions` - Create transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Categories

- `GET /api/categories` - List categories
- `GET /api/categories/{id}` - Get category details
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

## Request Examples

### Create Account

```javascript
POST /api/accounts
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "name": "My Savings",
  "type": "BANK",
  "balance": 1000.00,
  "currency": "USD"
}
```

### Create Transaction

```javascript
POST /api/transactions
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "accountId": 1,
  "categoryId": 5,
  "type": "EXPENSE",
  "amount": 50.00,
  "description": "Grocery shopping",
  "transactionDate": "2024-01-15"
}
```

### Update User Profile

```javascript
PUT /api/users/1
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "fullName": "John Smith",
  "avatarUrl": "https://example.com/avatar.jpg"
}
```

## Data Types

### Account Types

- `CASH` - Cash account
- `BANK` - Bank account
- `CREDIT_CARD` - Credit card

### Transaction Types

- `INCOME` - Money received
- `EXPENSE` - Money spent

### Date Format

- ISO 8601: `YYYY-MM-DD` for dates
- ISO 8601: `YYYY-MM-DDTHH:mm:ss` for timestamps
- Timezone: Asia/Ho_Chi_Minh (configured in backend)

## CORS Configuration

- Development: All origins allowed
- Production: Specific frontend domain only
- Credentials: Allowed
- Methods: GET, POST, PUT, DELETE, OPTIONS

## Best Practices

### Token Management

1. Store access token in memory (state management)
2. Store refresh token in secure storage (encrypted)
3. Implement automatic token refresh before expiry
4. Clear tokens on logout
5. Handle token expiration gracefully

### API Client Setup

```javascript
// Example using Axios
import axios from 'axios';

const apiClient = axios.create({
  baseURL: process.env.API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - add auth token
apiClient.interceptors.request.use(
  config => {
    const token = getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error),
);

// Response interceptor - handle errors
apiClient.interceptors.response.use(
  response => response.data,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        await refreshAccessToken();
        return apiClient(originalRequest);
      } catch (refreshError) {
        redirectToLogin();
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  },
);
```

### Error Display

- Show user-friendly error messages
- Log detailed errors to console (development)
- Send error reports to monitoring service (production)
- Provide retry options for network errors
- Show validation errors next to form fields

### Loading States

- Show loading indicators during API calls
- Disable submit buttons while processing
- Handle slow network gracefully
- Implement request timeouts (10 seconds recommended)

### Caching Strategy

- Cache user profile data
- Cache categories and account lists
- Invalidate cache on updates
- Use optimistic updates for better UX
- Implement offline support if needed

## Testing API Integration

### Using Swagger UI

Access interactive API documentation:

- Development: `http://localhost:8080/swagger-ui.html`

### Using cURL

```bash
# Login with Google (requires valid Google token)
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"idToken": "google-token"}'

# Get current user
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer {accessToken}"

# Create account
curl -X POST http://localhost:8080/api/accounts \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Account", "type": "CASH", "balance": 100}'
```

## Troubleshooting

### CORS Errors

- Ensure backend CORS is configured for your frontend domain
- Check that credentials are included in requests
- Verify OPTIONS preflight requests succeed

### 401 Unauthorized

- Check token is included in Authorization header
- Verify token hasn't expired
- Ensure token format is `Bearer {token}`
- Try refreshing the token

### 403 Forbidden

- User doesn't have permission for this resource
- Verify user is accessing their own resources
- Check user account is active

### Network Errors

- Verify backend is running
- Check API base URL is correct
- Ensure no firewall blocking requests
- Check network connectivity

## Security Considerations

1. Never log tokens or sensitive data
2. Use HTTPS in production
3. Implement CSRF protection if using cookies
4. Validate all user inputs client-side
5. Don't trust client-side validation alone
6. Implement rate limiting on frontend
7. Handle sensitive data carefully
8. Clear tokens on logout
9. Implement session timeout warnings
10. Use secure storage for tokens
