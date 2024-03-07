

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


