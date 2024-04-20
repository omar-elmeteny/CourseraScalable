
 CREATE TABLE IF NOT EXISTS users (
     user_id SERIAL PRIMARY KEY,
     username VARCHAR(255) UNIQUE NOT NULL,
     email VARCHAR(255) UNIQUE NOT NULL,
     password_hash VARCHAR(255) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 );

 -- create a table to store Social Media Information
 CREATE TABLE IF NOT EXISTS social_media_info (
     user_id INT REFERENCES users(user_id),
     social_media_name VARCHAR(50) NOT NULL CHECK (social_media_name IN ('Facebook', 'Twitter', 'LinkedIn', 'Instagram', 'Other')),
     social_media_link VARCHAR(255) NOT NULL,
     PRIMARY KEY (user_id, social_media_name)
 );
 --