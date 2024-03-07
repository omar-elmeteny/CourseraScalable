
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

-- Create register_user procedure
CREATE PROCEDURE register_user(
    p_username VARCHAR(255),
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255)
)
AS
BEGIN
    INSERT INTO users (username, email, password_hash) VALUES (@p_username, @p_email, @p_password_hash);
END;

-- Create register_user procedure
--CREATE OR REPLACE PROCEDURE register_user(
--    p_username VARCHAR(255),
--    p_email VARCHAR(255),
--    p_password_hash VARCHAR(255)
--)
--LANGUAGE plpgsql
--AS $$
--BEGIN
--    INSERT INTO users (username, email, password_hash) VALUES (p_username, p_email, p_password_hash);
--END;
--$$;

--
--
---- Create get_user_by_email procedure to retrieve user by email
--CREATE OR REPLACE PROCEDURE get_user_by_email(
--    p_email VARCHAR(255),
--    OUT user_data JSON
--)
--LANGUAGE plpgsql
--AS $$
--BEGIN
--    SELECT to_json(users) INTO user_data FROM users WHERE email = p_email;
--END;
--$$;
--
--
---- Authenticate user
--CREATE OR REPLACE PROCEDURE authenticate_user(
--    p_email VARCHAR(255),
--    p_password_hash VARCHAR(255),
--    OUT user_data JSON
--)
--LANGUAGE plpgsql
--AS $$
--BEGIN
--    SELECT to_json(users) INTO user_data FROM users WHERE email = p_email AND password_hash = p_password_hash;
--END;
--$$;
--
--
---- Change user password
--CREATE OR REPLACE PROCEDURE change_user_password(
--    p_email VARCHAR(255),
--    p_new_password_hash VARCHAR(255)
--)
--LANGUAGE plpgsql
--AS $$
--BEGIN
--    UPDATE users SET password_hash = p_new_password_hash WHERE email = p_email;
--END;
--$$;

---- Test procedures
---- Register a new user
--CALL register_user('TestUser', 'testuser@gmail.com', 'testpassword');
--
---- Declare user_data variable to store user data
--
--DO $$
--DECLARE
--    user_data JSON;
--BEGIN
--    CALL authenticate_user('testuser@gmail.com', 'testpassword', user_data);
--    RAISE NOTICE 'Output value: %', user_data;
--END $$;
--
---- Change user password
--
--DO $$
--DECLARE
--    user_data JSON;
--BEGIN
--    CALL change_user_password('testuser@gmail.com', 'newpassword');
--    CALL authenticate_user('testuser@gmail.com', 'newpassword', user_data);
--    RAISE NOTICE 'Output value: %', user_data;
--    CALL authenticate_user('testuser@gmail.com', 'testpassword', user_data);
--    RAISE NOTICE 'Output value: %', user_data;
--END $$;

