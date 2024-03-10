CREATE OR REPLACE FUNCTION insert_user_activity_log(
    p_user_id INT,
    p_activity_type VARCHAR(50),
    p_activity_description TEXT
) RETURNS VOID AS $$
BEGIN
INSERT INTO user_activity_logs (
    user_id, activity_type, activity_description
) VALUES (
             p_user_id, p_activity_type, p_activity_description
         );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_user_activity_logs(
    p_user_id INT
) RETURNS TABLE (
    log_id INT,
    user_id INT,
    activity_type VARCHAR(50),
    activity_description TEXT,
    activity_date TIMESTAMP
) AS $$
BEGIN
RETURN QUERY
SELECT
    user_activity_logs.log_id,
    user_activity_logs.user_id,
    user_activity_logs.activity_type,
    user_activity_logs.activity_description,
    user_activity_logs.activity_date
FROM user_activity_logs
WHERE user_activity_logs.user_id = p_user_id
ORDER BY user_activity_logs.activity_date DESC;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION delete_user_activity_log(
    p_log_id INT
) RETURNS VOID AS $$
BEGIN
DELETE FROM user_activity_logs WHERE log_id = p_log_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_by_user_id_ordered_by_activity_date_desc(
    p_user_id INT
) RETURNS TABLE (
    user_id INT,
    log_id INT,
    activity_type VARCHAR(50),
    activity_description TEXT,
    activity_date TIMESTAMP
) AS $$
BEGIN
RETURN QUERY
SELECT
    ual.user_id,  -- Specify the table alias for log_id
    ual.log_id,
    ual.activity_type,
    ual.activity_description,
    ual.activity_date
FROM user_activity_logs as ual
WHERE p_user_id is null or  ual.user_id = p_user_id
ORDER BY ual.activity_date DESC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_user_profile(
    p_user_id INT,
    p_first_name VARCHAR(50),
    p_last_name VARCHAR(50),
    p_bio TEXT,
    p_profile_photo_url VARCHAR(255),
    p_is_email_verified BOOLEAN,
    p_is_phone_verified BOOLEAN,
    p_phone_number VARCHAR(15),
    p_date_of_birth DATE
) RETURNS VOID AS $$
BEGIN
INSERT INTO user_profile (
    user_id, first_name, last_name, bio, profile_photo_url,
    is_email_verified, is_phone_verified, phone_number, date_of_birth
) VALUES (
             p_user_id, p_first_name, p_last_name, p_bio, p_profile_photo_url,
             p_is_email_verified, p_is_phone_verified, p_phone_number, p_date_of_birth
         );
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION update_user_profile(
    p_profile_id INT,
    p_first_name VARCHAR(50),
    p_last_name VARCHAR(50),
    p_bio TEXT,
    p_profile_photo_url VARCHAR(255),
    p_is_email_verified BOOLEAN,
    p_is_phone_verified BOOLEAN,
    p_phone_number VARCHAR(15),
    p_date_of_birth DATE
) RETURNS VOID AS $$
BEGIN
UPDATE user_profile as up
SET
    up.first_name = p_first_name,
    up.last_name = p_last_name,
    up.bio = p_bio,
    up.profile_photo_url = p_profile_photo_url,
    up.is_email_verified = p_is_email_verified,
    up.is_phone_verified = p_is_phone_verified,
    up.phone_number = p_phone_number,
    up.date_of_birth = p_date_of_birth
WHERE profile_id = p_profile_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION delete_user_profile(p_profile_id INT) RETURNS VOID AS $$
BEGIN
DELETE FROM user_profile as up WHERE up.profile_id = p_profile_id;
END;
$$ LANGUAGE plpgsql;




CREATE OR REPLACE FUNCTION get_user_profile(p_profile_id INT) RETURNS TABLE (
    profile_id INT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    bio TEXT,
    profile_photo_url VARCHAR(255),
    is_email_verified BOOLEAN,
    is_phone_verified BOOLEAN,
    phone_number VARCHAR(15),
    date_of_birth DATE
) AS $$
BEGIN
RETURN QUERY
SELECT
    up.*
FROM user_profile as up
WHERE up.profile_id = p_profile_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_user_profile_user_id(p_user_id INT) RETURNS TABLE (
    profile_id INT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    bio TEXT,
    profile_photo_url VARCHAR(255),
    is_email_verified BOOLEAN,
    is_phone_verified BOOLEAN,
    phone_number VARCHAR(15),
    date_of_birth DATE
) AS $$
BEGIN
RETURN QUERY
SELECT
    up.*
FROM user_profile as up
WHERE up.user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_users_by_filters(
    p_user_id INT DEFAULT NULL,
    p_first_name VARCHAR(50) DEFAULT NULL,
    p_last_name VARCHAR(50) DEFAULT NULL,
    p_is_email_verified BOOLEAN DEFAULT NULL,
    p_is_phone_verified BOOLEAN DEFAULT NULL,
    p_phone_number VARCHAR(15) DEFAULT NULL,
    p_date_of_birth DATE DEFAULT NULL
) RETURNS TABLE (
    profile_id INT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_email_verified BOOLEAN,
    is_phone_verified BOOLEAN,
    phone_number VARCHAR(15),
    date_of_birth DATE
) AS $$
BEGIN
RETURN QUERY
SELECT
    up.*
FROM user_profile as up
WHERE
    (p_user_id IS NULL OR up.user_id = p_user_id) AND
    (p_first_name IS NULL OR up.first_name = p_first_name) AND
    (p_last_name IS NULL OR up.last_name = p_last_name) AND
    (p_is_email_verified IS NULL OR up.is_email_verified = p_is_email_verified) AND
    (p_is_phone_verified IS NULL OR up.is_phone_verified = p_is_phone_verified) AND
    (p_phone_number IS NULL OR up.phone_number = p_phone_number) AND
    (p_date_of_birth IS NULL OR up.date_of_birth = p_date_of_birth);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE assign_role_to_user(user_id_var INT, role_name_var VARCHAR)
AS $$
BEGIN
INSERT INTO user_roles (user_id, role_id)
SELECT users.user_id, roles.role_id
FROM users, roles
WHERE users.user_id = user_id_var
  AND roles.role_name = role_name_var;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE grant_permission_to_role(role_name_var VARCHAR, permission_name_var VARCHAR)
AS $$
BEGIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT roles.role_id, permissions.permission_id
FROM roles, permissions
WHERE roles.role_name = role_name_var
  AND permissions.permission_name = permission_name_var;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE check_user_permission(
	user_id_var INT,
	permission_name_var VARCHAR,
    inout exist bool
)
AS $$
BEGIN
SELECT EXISTS(
    SELECT 1
    FROM user_roles
             JOIN role_permissions ON user_roles.role_id = role_permissions.role_id
             JOIN permissions ON role_permissions.permission_id = permissions.permission_id
    WHERE user_roles.user_id = user_id_var
      AND permissions.permission_name = permission_name_var
) into exist;
END;
$$ LANGUAGE plpgsql;


-- TEST Procudures
-- CALL assign_role_to_user(1, 'admin');
-- CALL grant_permission_to_role('admin', 'manage_users');

-- DO
-- $$
-- Declare
-- exist bool;
-- begin
--    CALL check_user_permission(1, 'manage_users', exist);
--    RAISE NOTICE 'Output value: %', exist;
-- end
-- $$;

-- Select * from user_roles;
-- Select * from role_permissions;

   -- Create register_user procedure
CREATE OR REPLACE PROCEDURE register_user(
    p_username VARCHAR(255),
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
INSERT INTO users (username, email, password_hash) VALUES (p_username, p_email, p_password_hash);
END;
$$;



-- Create get_user_by_email procedure to retrieve user by email
CREATE OR REPLACE PROCEDURE get_user_by_email(
    p_email VARCHAR(255),
    inout user_data JSON
)
LANGUAGE plpgsql
AS $$
BEGIN
SELECT to_json(users) INTO user_data FROM users WHERE email = p_email;
END;
$$;


-- Authenticate user
CREATE OR REPLACE PROCEDURE authenticate_user(
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255),
    inout user_data JSON
)
LANGUAGE plpgsql
AS $$
BEGIN
SELECT to_json(users) INTO user_data FROM users WHERE email = p_email AND password_hash = p_password_hash;
END;
$$;


-- Change user password
CREATE OR REPLACE PROCEDURE change_user_password(
    p_email VARCHAR(255),
    p_new_password_hash VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
UPDATE users SET password_hash = p_new_password_hash WHERE email = p_email;
END;
$$;

-- Test procedures
-- Register a new user
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
--
