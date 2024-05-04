# User Management APIs


## User Scenarios

### POST /api/v1/user/register
- Roles: `Student` and `Instructor`
- Anonymous: `true`
- Description: Register a new user
- Request:
```json
{
  "firstName": "string/required",
  "lastName": "string/required",
  "username": "string/unique/required",
  "email": "string/email format/unique/required",
  "password": "string/required",
  "date_of_birth": "string(yyyy-MM-dd)/past date/min age???/required",
  "phone_number": "string/unique/phone number format/required",
  "profile_picture": "optional/base64/saved to media server/save url in db",
  "bio": "string/optional"
}
```
- The user has only student role. When they create a course they are assigned instructor role and the admin approves the course they are assigned to instructor role.
Response 201 created (400 if bad request)
- `is_email_verified` and `is_phone_verified` should be false
- A one time password should be sent to the email for verification
- Another one time password should be sent to the phone number for verification
- 400 Response: ArrayOfStrings error messages


### POST /api/v1/user/verify-email
- Roles: `Student` and `Instructor`
- Anonymous: `true`
- Description: Verify the email address
- Request:
```json
{
  "email": "string/required",
  "otp": "string/required"
}
```
- Response 200 (or 400 if invalid otp)
- 400 Response: ArrayOfStrings error messages

### POST /api/v1/user/verify-phone
- Roles: `Student` and `Instructor`
- Anonymous: `true`
- Description: Verify the phone number
- Request:
```json
{
  "phone_number": "string/required",
  "otp": "string/required"
}
```
- Response 200 (or 400 if invalid otp)
- 400 Response: ArrayOfStrings error messages


### POST /api/v1/user/login
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `true`
- Description: Login a user
- Request:
```json
{
  "username": "string/required",
  "password": "string/required"
}
```
Response 200 (or 400 if invalid credentials or 
email is not verified 
or phone is not verified 
or account is deleted
or max login attempts reached (and lock time is not expired)
)
```json
{
    "accessToken": "jwtToken",
    "refreshToken": "jwtToken",
    "userId": "userId",
    "expires": "token expiry date",
    "tokenType": "Bearer"
}
```
- 400 Response: ArrayOfStrings error messages

### GET /api/v1/profile
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Get user current user profile
- Request: None
- Response 200
```json
{
    "id": "userId",
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "date_of_birth": "string",
    "phone_number": "string",
    "profile_picture": "base64",
    "bio": "string"
}
```

### POST /api/v1/profile
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Update the current logged in user profile
- Request:
```json
{
    "firstName": "string",
    "lastName": "string",
    "date_of_birth": "string",
    "profile_picture": "base64",
    "bio": "string"
}
```
- 400 Response: ArrayOfStrings error messages

### DELETE /api/v1/profile
- Roles: `Student` and `Instructor`
- Anonymous: `false`
- Description: Delete the current logged-in user profile
- Request: None
- Response 200
- The user should be logged out and the account marked as deleted in the database (do NOT actually delete the account)
- The user information `username`, `first_name`,  `last_name` (for example `user_12345`, `<deleted>`, `<deleted>`)
- `profile_picture`, `bio`, `email` and `phone_number` should be set to NULL values.
- The profile picture should be deleted from the media server

### POST /api/v1/user/forgot-password-email
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `true`
- Description: Send a one time password to the email address
- Request:
```json
{
    "email": "string/required"
}
```
- Should always return 200 even if the email does not exist
- 400 Response: ArrayOfStrings error messages (only in case of invalid email address)

### POST /api/v1/user/forgot-password-phone
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `true`
- Description: Send a one time password to the phone number
- Request:
```json
{
    "phone": "string/required"
}
```
- Should always return 200 even if the phone number does not exist
- 400 Response: ArrayOfStrings error messages (only in case of invalid phone number)

### POST /api/v1/user/reset-password
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `true`
- Description: Reset the password
- Request:
```json
{
    "phone": "string/required if email is not provided",
    "email": "string/required if phone is not provided",
    "otp": "string/required",
    "password": "string/required"
}
```
- Response 200 (or 400 if invalid otp)
- 400 Response: ArrayOfStrings error messages

### POST /api/v1/user/change-password
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Change the password of the current logged-in user
- Request:
```json
{
    "oldPassword": "string/required",
    "newPassword": "string/required"
}
```
- Response 200 (or 400 if invalid old password)
- 400 Response: ArrayOfStrings error messages

### POST /api/v1/user/logout
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Logout the current logged-in user (deletes all login cookies (if any))
- Request: None
- Response 200
- The user should be logged out

## Admin Scenarios

### GET /api/v1/admin/search-users
- Roles: `Admin`
- Anonymous: `false`
- Description: Search for users
Query Parameters:
- `firstName` (string): search query
- `lastName` (string): search query
- `username` (string): search query
- `email` (string): search query
- `phone_number` (string): search query
- `size` (int): number of users to return (default 10)
- `page` (int): page number (zero based) (default 0)
- Response 200
```json
{
    "totalNumberOfProfiles": "total number of users",
    "profiles": [
        {
            "id": "userId",
            "firstName": "string",
            "lastName": "string",
            "username": "string",
            "email": "string",
            "date_of_birth": "string",
            "phone_number": "string",
            "profile_picture_url": "base64",
            "bio": "string",
            "roles": "arrayOfStrings"
        }
    ],
    "success": "boolean",
    "errorMessages": "arrayOfStrings"
}
```

### POST /api/v1/admin/reset-password
- Roles: `Admin`
- Anonymous: `false`
- Description: Reset the password of a user
- Request:
```json
{
    "userId": "string/required",
    "password": "string/required"
}
```
- Response 200
- 400 Response: ArrayOfStrings error messages (bad userId or bad password)

### DELETE /api/v1/admin/{userId}
- Roles: `Admin`
- Anonymous: `false`
- Description: Delete an admin (not the current logged-in admin)
- Response 200
- 400 Response: ArrayOfStrings error messages (bad userId or cannot delete the current logged-in admin)
- Only the admin role should be deleted. The user should still have the student role.

### POST /api/v1/admin/create-admin
- Roles: `Admin`
- Anonymous: `false`
- Description: Create a new admin
- Request:
```json
{
    "firstName": "string/required",
    "lastName": "string/required",
    "username": "string/unique/required",
    "email": "string/email format/unique/required",
    "password": "string/required",
    "date_of_birth": "string(yyyy-MM-dd)/past date/min age???/required",
    "phone_number": "string/unique/phone number format/required",
    "profile_picture": "optional/base64/saved to media server/save url in db",
    "bio": "string/optional"
}
```
- Response 201 created (400 if bad request)
- `is_email_verified` and `is_phone_verified` should be false
- A one time password should be sent to the email for verification
- Another one time password should be sent to the phone number for verification
- The user is assigned `Admin` and `Student` role
- 400 Response: ArrayOfStrings error messages


### POST /api/v1/admin/unlock-account
- Roles: `Admin`
- Anonymous: `false`
- Description: Remove the lock from a user account
- Request:
```json
{
    "userId": "string/required"
}
```
- Response 200
- 400 Response: ArrayOfStrings error messages (bad userId)

### POST /api/v1/admin/lock-account
- Roles: `Admin`
- Anonymous: `false`
- Description: Lock a user account
- Request:
```json
{
    "userId": "string/required",
    "reason": "string/required",
    "lockTime": "duration in minutes/required"
}
```
- Response 200
- 400 Response: ArrayOfStrings error messages (bad userId, missing reason or lockTime, etc.)

## Nice to have

### POST /api/v1/user/change-email
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Change the email address of the current logged-in user
- Request:
```json
{
    "email": "string/required"
}
```
- Response 200 (or 400 if invalid email)
- 400 Response: ArrayOfStrings error messages
- The user should get an email with a one time password to verify the new email address. 
- The new email address should be saved in the database in another field (not email field).

### POST /api/v1/user/change-phone
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Change the phone number of the current logged-in user
- Request:
```json
{
    "phone_number": "string/required"
}
```
- Response 200 (or 400 if invalid phone number)
- 400 Response: ArrayOfStrings error messages
- The user should get an SMS with a one time password to verify the new phone number.
- The new phone number should be saved in the database in another field (not phone_number field).


### POST /api/v1/user/verify-new-email
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Verify the new email address
- Request:
```json
{
    "newEmail": "string/required",
    "otp": "string/required"
}
```
- Response 200 (or 400 if invalid otp)
- 400 Response: ArrayOfStrings error messages
- The new email address should be saved in the email field in the database.

### POST /api/v1/user/verify-new-phone
- Roles: `Admin`, `Student` and `Instructor`
- Anonymous: `false`
- Description: Verify the new phone number
- Request:
```json
{
    "newPhone": "string/required",
    "otp": "string/required"
}
```
- Response 200 (or 400 if invalid otp)
- 400 Response: ArrayOfStrings error messages
- The new phone number should be saved in the phone_number field in the database.

