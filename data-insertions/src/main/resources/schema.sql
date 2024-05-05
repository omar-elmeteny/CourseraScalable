-- Create a table to store user information
CREATE TABLE IF NOT EXISTS users
(
    user_id            SERIAL PRIMARY KEY,
    username           VARCHAR(255) UNIQUE NOT NULL,
    email              VARCHAR(255) UNIQUE NULL,
    password_hash      VARCHAR(255)        NOT NULL,
    first_name         VARCHAR(50)         NOT NULL,
    last_name          VARCHAR(50)         NOT NULL,
    date_of_birth      DATE                NOT NULL,
    registration_date  TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    is_email_verified  BOOLEAN             NOT NULL DEFAULT FALSE,
    profile_photo_url  VARCHAR(255),
    phone_number       VARCHAR(15) UNIQUE  NULL,
    bio                TEXT,
    is_deleted         BOOLEAN             NOT NULL DEFAULT FALSE,
    is_locked          BOOLEAN             NOT NULL DEFAULT FALSE,
    lock_reason        VARCHAR(255)        DEFAULT NULL,
    lockout_expires    TIMESTAMP                    DEFAULT NULL,
    failed_login_count INT                 NOT NULL DEFAULT 0
);

-- Create a table to store roles
CREATE TABLE IF NOT EXISTS roles
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- Populate predefined roles (e.g., student, instructor, admin)
INSERT INTO roles (role_name)
VALUES ('student'),
       ('instructor'),
       ('admin')
ON CONFLICT DO NOTHING;

-- Create a table to link users with roles (user can have multiple roles)
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id INT REFERENCES users (user_id),
    role_id INT REFERENCES roles (role_id),
    PRIMARY KEY (user_id, role_id)
);


-- Create a table for login history (to track successful,failed login attempts, to be able to lock account(for 3 fail attempts) and timestamps)
CREATE TABLE IF NOT EXISTS login_history
(
    history_id   SERIAL PRIMARY KEY,
    user_id      INT REFERENCES users (user_id),
    login_status BOOLEAN     NOT NULL DEFAULT FALSE, -- TRUE for successful login, FALSE for failed login
    timestamp    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    ip_address   VARCHAR(50) NOT NULL,
    user_agent   TEXT        NOT NULL
);


-- Table for password reset requests
CREATE TABLE IF NOT EXISTS one_time_passwords
(
    request_id         SERIAL PRIMARY KEY,
    user_id            INT REFERENCES users (user_id),
    password_hash      VARCHAR(255) UNIQUE NOT NULL,
    request_date       TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    expiry_date        TIMESTAMP           NOT NULL,
    remaining_attempts INT                 NOT NULL DEFAULT 3
);
