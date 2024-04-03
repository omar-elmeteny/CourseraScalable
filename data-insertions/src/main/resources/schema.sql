-- Create a table to store user information
CREATE TABLE IF NOT EXISTS users (
                                     user_id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    profile_photo_url VARCHAR(255),
    phone_number VARCHAR(15) UNIQUE
    );

CREATE TABLE IF NOT EXISTS user_profile (
                                            profile_id SERIAL PRIMARY KEY,
                                            user_id INT REFERENCES users(user_id),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bio TEXT,
    profile_photo_url VARCHAR(255),
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_number VARCHAR(15) UNIQUE,
    date_of_birth DATE NOT NULL
    );

-- create a table to store Social Media Information
CREATE TABLE IF NOT EXISTS social_media_info (
                                                 user_id INT REFERENCES users(user_id),
    social_media_name VARCHAR(50) NOT NULL CHECK (social_media_name IN ('Facebook', 'Twitter', 'LinkedIn', 'Instagram', 'Other')),
    social_media_link VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, social_media_name)
    );

-- create a table for events
CREATE TABLE IF NOT EXISTS events (
                                      event_id SERIAL PRIMARY KEY,
                                      event_name VARCHAR(255) NOT NULL,
    event_description TEXT NOT NULL,
    event_date TIMESTAMP NOT NULL,
    course_id INT DEFAULT NULL
    );


-- create a table for events notifications
CREATE TABLE IF NOT EXISTS event_notifications (
                                                   notification_id SERIAL PRIMARY KEY,
                                                   event_id INT REFERENCES events(event_id),
    user_id INT REFERENCES users(user_id),
    notification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (event_id, user_id)
    );

-- Create a table to store roles
CREATE TABLE IF NOT EXISTS roles (
                                     role_id SERIAL PRIMARY KEY,
                                     role_name VARCHAR(50) UNIQUE NOT NULL
    );

-- Populate predefined roles (e.g., student, instructor, admin)
INSERT INTO roles (role_name) VALUES
                                  ('student'),
                                  ('instructor'),
                                  ('admin')
    ON CONFLICT DO NOTHING;

-- Create a table to link users with roles (user can have multiple roles)
CREATE TABLE IF NOT EXISTS  user_roles(
                                          user_id INT REFERENCES users(user_id),
    role_id INT REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
    );



---- Create a table for user subscriptions
--CREATE TABLE IF NOT EXISTS user_subscriptions(
--    subscription_id INT,
--    user_id INT REFERENCES users(user_id),
--    subscription_start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    PRIMARY KEY (subscription_id, user_id)
--);
--
---- Create a table for course enrollment
--CREATE TABLE IF NOT EXISTS  course_enrollment (
--    enrollment_id SERIAL PRIMARY KEY,
--    user_id INT REFERENCES users(user_id),
--    course_id INT , -- Assuming you have a courses table
--    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    progress INT DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
--    status VARCHAR(50) DEFAULT 'Active' CHECK (status IN ('Active', 'Suspended', 'Completed')),
--    UNIQUE (user_id, course_id),
--    subscription_id INT REFERENCES user_subscriptions(subscription_id)
--);

-- Create a table for login history (to track successful,failed login attempts, to be able to lock account(for 3 fail attempts) and timestamps)
CREATE TABLE IF NOT EXISTS  login_history (
                                              history_id SERIAL PRIMARY KEY,
                                              user_id INT REFERENCES users(user_id),
    login_status BOOLEAN NOT NULL DEFAULT FALSE, -- TRUE for successful login, FALSE for failed login
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50) NOT NULL,
    user_agent TEXT NOT NULL
    );


CREATE TABLE IF NOT EXISTS problem_reports (
                                               report_id SERIAL PRIMARY KEY,
                                               user_id INT REFERENCES users(user_id),
    problem_description TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'Open' CHECK (status IN ('Open', 'In Progress', 'Closed')),
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create a table for permissions
CREATE TABLE IF NOT EXISTS  permissions (
                                            permission_id SERIAL PRIMARY KEY,
                                            permission_name VARCHAR(50) UNIQUE NOT NULL
    );

-- Populate predefined permissions (e.g., create_course, edit_course, enroll_student)
INSERT INTO permissions (permission_name) VALUES
                                              ('create_course'),
                                              ('edit_course'),
                                              ('enroll_student'),
                                              ('delete_course'),
                                              ('view_reports'),
                                              ('manage_users')
    ON CONFLICT DO NOTHING;

-- Create a table to link roles with permissions
CREATE TABLE IF NOT EXISTS role_permissions (
                                                role_id INT REFERENCES roles(role_id),
    permission_id INT REFERENCES permissions(permission_id),
    PRIMARY KEY (role_id, permission_id)
);

INSERT INTO role_permissions (role_id, permission_id) VALUES
                                                          (1, 3), -- Student role can enroll_student
                                                          (2, 1), -- Instructor role can create_course
                                                          (2, 2), -- Instructor role can edit_course
                                                          (2, 4), -- Instructor role can delete_course
                                                          (3, 5), -- Admin role can view_reports
                                                          (3, 6) -- Admin role can manage_users
    ON CONFLICT DO NOTHING;

-- Table for password reset requests
CREATE TABLE IF NOT EXISTS password_reset_requests (
                                                       request_id SERIAL PRIMARY KEY,
                                                       user_id INT REFERENCES users(user_id),
    reset_token VARCHAR(255) UNIQUE NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Table to store user activity logs
CREATE TABLE IF NOT EXISTS user_activity_logs(
                                                 log_id SERIAL PRIMARY KEY,
                                                 user_id INT REFERENCES users(user_id),
    activity_description TEXT NOT NULL,
    activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activity_type VARCHAR(50) DEFAULT 'profile_update' CHECK (activity_type IN ('login', 'logout', 'profile_update', 'password_change', 'email_verification', 'phone_verification', 'role_update', 'course_enrollment', 'course_completion', 'event_registration', 'event_participation', 'social_media_link'))
    );



