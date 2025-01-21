

# URL Shortener API

This project is a URL Shortener API built using Spring Boot. It provides functionalities such as user registration and login, shortening URLs, redirection to original links, and analytics for tracking the number of clicks on each shortened URL.

## Features

- **User Registration**: Users must register with the system before shortening URLs. The registration process requires sending a POST request with user details.
- **Login with JWT**: After registering, users must log in using their credentials. The system returns a JWT token, which is used to authenticate future requests.
- **URL Shortening**: Once logged in, users can shorten URLs. The system generates a short URL and redirects the user to the original link.
- **Analytics**: Provides an endpoint to get the number of clicks on a shortened URL within a given date range, and to get all clicks across all URLs.

## Endpoints

### 1. User Registration
**URL**: `/api/auth/public/register`  
**Method**: `POST`  
**Request Body**:
```json
{
  "username": "user123",
  "password": "password123",
  "email": "user@example.com"
}
```
**Response**:
- `201 Created` with user information upon successful registration.

### 2. User Login
**URL**: `/api/auth/public/login`  
**Method**: `POST`  
**Request Body**:
```json
{
  "username": "user123",
  "password": "password123"
}
```
**Response**:
- `200 OK` with JWT token if the login is successful.
  
```json
{
  "token": "jwt_token_here"
}
```

### 3. Create Short URL
**URL**: `/api/url/create`  
**Method**: `POST`  
**Request Headers**:  
- `Authorization: Bearer <JWT_Token>` (JWT token required for authentication)

**Request Body**:
```json
{
  "originalUrl": "https://www.example.com"
}
```
**Response**:
- `200 OK` with shortened URL.
```json
{
  "shortUrl": "http://short.ly/abcd1234"
}
```

### 4. URL Redirection
**URL**: `/api/url/{shortUrl}`  
**Method**: `GET`  
**Response**:  
Redirects to the original URL for the given short URL.

### 5. Analytics - Get Clicks by Date Range
**URL**: `/api/analytics/clicks`  
**Method**: `GET`  
**Request Parameters**:
- `startDate`: The start date for the range (YYYY-MM-DD)
- `endDate`: The end date for the range (YYYY-MM-DD)

**Response**:
- `200 OK` with the number of clicks in the given date range.

### 6. Analytics - Get All Clicks for All URLs
**URL**: `/api/analytics/all-clicks`  
**Method**: `GET`  
**Response**:
- `200 OK` with a list of all URLs and their associated click counts.

## Postman Collection

You can view and interact with all the routes in this project using the Postman collection provided below:

[**URL Shortener Postman Collection**](https://www.postman.com/avionics-explorer-29622376/dmadinidvidual/collection/6e2w8yh/url-shortener?action=share&creator=29599021)

## Technologies Used
- **Spring Boot**: For building the backend API.
- **JWT (JSON Web Tokens)**: For authentication and authorization.
- **Lombok**: For reducing boilerplate code in Java classes.

## How to Run

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   ```

2. **Build the Project**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Test the Endpoints** using tools like Postman or curl.

## Conclusion

This API provides a simple way for users to shorten URLs and track clicks on those URLs. The JWT-based authentication ensures that only authorized users can create short URLs, while the analytics feature helps users to track the performance of their shortened links.

---

This README now includes the Postman collection link for easier interaction with the API.
