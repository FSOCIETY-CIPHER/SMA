# Spring Boot Social Media API
This project is a Spring Boot-based Social Media API. It's designed to handle basic social media functionalities such as user management, posts, and comments, along with follower relationships. The project is structured to allow for future expansion, including advanced features like post likes/comments and the ability to revert these features if necessary.

# Features
User Management: CRUD operations for users, including properties like username, email, profile picture, and follower/following relationships.
Post Management: Users can create, read, update, and delete posts. Each post contains content, creation date, and is linked to the user who created it.
Comment Management: Functionality to add comments to posts.
Follow/Unfollow: Users can follow or unfollow other users.
Pagination and Sorting: Implemented for listing posts and comments for better data management and user interface.
Spring Security and JWT: User authentication and authorization are handled using Spring Security and JSON Web Tokens (JWT).
Unit Testing: Ensures the correctness of API endpoints.
Database: Integration with a database (e.g., H2, MySQL, PostgreSQL) for persistent data storage.

# Prerequisites
JDK 17
Maven

# Built With
Spring Boot - The web framework used.
Maven - Dependency Management.
Spring Security - Authentication and Authorization.
JWT - JSON Web Tokens for secure API access.

# Authors
Odunsi Oluwaseyi - Initial Version




