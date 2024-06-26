CREATE OR REPLACE FUNCTION update_user_profile(
    p_user_id INT,
    p_first_name VARCHAR(50),
    p_last_name VARCHAR(50),
    p_bio TEXT,
    p_profile_photo_url VARCHAR(255),
    p_phone_number VARCHAR(15),
    p_date_of_birth DATE
) RETURNS VOID AS
$$
BEGIN
    UPDATE users as u
    SET first_name        = p_first_name,
        last_name         = p_last_name,
        bio               = p_bio,
        profile_photo_url = p_profile_photo_url,
        phone_number      = p_phone_number,
        date_of_birth     = p_date_of_birth
    WHERE u.user_id = p_user_id
      AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE set_email_verified(p_user_id INT) LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE users as u
    SET is_email_verified = true
    WHERE u.user_id = p_user_id
      AND u.is_deleted = false;
END;
$$;

CREATE OR REPLACE FUNCTION add_login_attempt(
    p_user_id INT,
    p_login_status BOOLEAN,
    p_ip_address VARCHAR(50),
    p_user_agent VARCHAR(255)
) RETURNS INT AS
$$
DECLARE
    failed_login_count INT;
BEGIN
    INSERT INTO login_history (user_id, login_status, ip_address, user_agent)
    VALUES (p_user_id, p_login_status, p_ip_address, p_user_agent);

    IF p_login_status = false THEN
        UPDATE users as u
        SET failed_login_count = u.failed_login_count + 1
        WHERE u.user_id = p_user_id
        RETURNING u.failed_login_count INTO failed_login_count;
    ELSE
        UPDATE users as u
        SET failed_login_count = 0
        WHERE u.user_id = p_user_id;
        failed_login_count := 0;
    END IF;
    RETURN failed_login_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_user(p_user_id INT) RETURNS VOID AS
$$
BEGIN

    DELETE
    FROM user_roles
    WHERE user_id = p_user_id;

    UPDATE users as u
    SET is_deleted        = true,
        first_name        = '<<deleted>>',
        last_name         = '<<deleted>>',
        email             = p_user_id::VARCHAR + '@deleted.com',
        bio               = NULL,
        phone_number      = NULL,
        date_of_birth     = '1900-01-01',
        profile_photo_url = NULL,
        is_email_verified = false,
        username          = '<<deleted>>' + user_id::VARCHAR,
        password_hash     = '<<deleted>>'
    WHERE u.user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_status(p_user_id INT)
    RETURNS TABLE
            (
                user_id            INT,
                username           VARCHAR(255),
                is_locked          BOOLEAN,
                is_deleted         BOOLEAN,
                failed_login_count INT,
                lock_reason        VARCHAR(255),
                lockout_expires    TIMESTAMP,
                is_email_verified  BOOLEAN,
                registration_date  TIMESTAMP,
                password_date      TIMESTAMP,
                password_hash      VARCHAR(255)
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.is_locked,
               u.is_deleted,
               u.failed_login_count,
               u.lock_reason,
               u.lockout_expires,
               u.is_email_verified,
               u.registration_date,
               u.password_date,
               u.password_hash
        FROM users as u
        WHERE u.user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE lock_user(
    p_user_id INT,
    p_reason VARCHAR(255),
    p_lockout_expires TIMESTAMP
) AS
$$
BEGIN
    UPDATE users as u
    SET is_locked       = true,
        lock_reason     = p_reason,
        lockout_expires = p_lockout_expires
    WHERE user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE unlock_user(p_user_id INT)
AS
$$
BEGIN
    UPDATE users as u
    SET is_locked       = false,
        lock_reason     = NULL,
        lockout_expires = NULL
    WHERE user_id = p_user_id
      AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_by_phone(p_phone_number varchar(15))
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                email             VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.email,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
        WHERE u.phone_number = p_phone_number
          AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS get_user_by_email(VARCHAR(255));

CREATE OR REPLACE FUNCTION get_user_by_email(p_email varchar(255))
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                email             VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.email,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
        WHERE u.email = p_email
          AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_by_id(p_user_id INT)
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                email             VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.email,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
        WHERE u.user_id = p_user_id
          AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_count_by_filters(
    p_first_name VARCHAR(50) DEFAULT NULL,
    p_last_name VARCHAR(50) DEFAULT NULL,
    p_phone_number VARCHAR(15) DEFAULT NULL,
    p_email VARCHAR(255) DEFAULT NULL
)
    RETURNS INT
AS
$$
DECLARE
    total_count INT;
BEGIN
    SELECT COUNT(*)
    FROM users as u
    WHERE u.is_deleted = false
      AND (p_email IS NULL OR u.email ILIKE '%' || p_email || '%')
      AND (p_first_name IS NULL OR u.first_name ILIKE '%' || p_first_name || '%')
      AND (p_last_name IS NULL OR u.last_name ILIKE '%' || p_last_name || '%')
      AND (p_phone_number IS NULL OR u.phone_number ILIKE '%' || p_phone_number || '%')
    INTO total_count;
    RETURN total_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_users_by_filters(
    p_first_name VARCHAR(50) DEFAULT NULL,
    p_last_name VARCHAR(50) DEFAULT NULL,
    p_phone_number VARCHAR(15) DEFAULT NULL,
    p_email VARCHAR(255) DEFAULT NULL,
    p_limit INT DEFAULT 10,
    p_offset INT DEFAULT 0
)
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                email             VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.email,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
        WHERE u.is_deleted = false
          AND (p_email IS NULL OR u.email ILIKE '%' || p_email || '%')
          AND (p_first_name IS NULL OR u.first_name ILIKE '%' || p_first_name || '%')
          AND (p_last_name IS NULL OR u.last_name ILIKE '%' || p_last_name || '%')
          AND (p_phone_number IS NULL OR u.phone_number ILIKE '%' || p_phone_number || '%')
        ORDER BY user_id
        LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_admin_users()
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
                 JOIN user_roles as ur ON u.user_id = ur.user_id
                 JOIN roles as r ON ur.role_id = r.role_id
        WHERE r.role_name = 'admin'
          AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE assign_role_to_user(user_id_var INT, role_name_var VARCHAR)
AS
$$
BEGIN
    INSERT INTO user_roles (user_id, role_id)
    SELECT users.user_id, roles.role_id
    FROM users,
         roles
    WHERE users.user_id = user_id_var
      AND roles.role_name = role_name_var;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE remove_role_from_user(user_id_var INT, role_name_var VARCHAR)
AS
$$
BEGIN
    DELETE
    FROM user_roles
    WHERE user_id = user_id_var
      AND role_id = (SELECT role_id FROM roles WHERE role_name = role_name_var);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_roles(p_user_id INT)
    RETURNS TABLE
            (
                role_name VARCHAR(50)
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT r.role_name
        FROM roles as r
                 JOIN user_roles as ur ON r.role_id = ur.role_id
        WHERE ur.user_id = p_user_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_user_by_username(p_username VARCHAR(255))
    RETURNS TABLE
            (
                user_id           INT,
                username          VARCHAR(255),
                email             VARCHAR(255),
                first_name        VARCHAR(50),
                last_name         VARCHAR(50),
                bio               TEXT,
                profile_photo_url VARCHAR(255),
                phone_number      VARCHAR(15),
                date_of_birth     DATE
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT u.user_id,
               u.username,
               u.email,
               u.first_name,
               u.last_name,
               u.bio,
               u.profile_photo_url,
               u.phone_number,
               u.date_of_birth
        FROM users as u
        WHERE u.username = p_username
          AND u.is_deleted = false;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION register_user(
    p_username VARCHAR(255),
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255),
    p_first_name VARCHAR(255),
    p_last_name VARCHAR(255),
    p_phone_number VARCHAR(15),
    p_date_of_birth DATE,
    p_profile_photo_url VARCHAR(255)
)
    RETURNS TABLE
            (
                user_id           INT,
                registration_date TIMESTAMP
            )
AS
$$
DECLARE
    user_id_var                   INT;
    DECLARE registration_date_var TIMESTAMP;
BEGIN
    INSERT INTO users (username,
                       email,
                       password_hash,
                       first_name,
                       last_name,
                       phone_number,
                       date_of_birth,
                       profile_photo_url)
    VALUES (p_username,
            p_email,
            p_password_hash,
            p_first_name,
            p_last_name,
            p_phone_number,
            p_date_of_birth,
            p_profile_photo_url)
    RETURNING users.user_id, users.registration_date INTO user_id_var, registration_date_var;
    RETURN QUERY SELECT user_id_var, registration_date_var;
END;
$$ LANGUAGE plpgsql;

-- Authenticate user
CREATE OR REPLACE FUNCTION authenticate_user(
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255)
) RETURNS VARCHAR(10)
AS
$$
DECLARE
    password_hash VARCHAR(255);
    user_locked   BOOLEAN;
BEGIN

    SELECT u.password_hash, u.is_locked
    INTO password_hash, user_locked
    FROM users as u
    WHERE u.email = p_email
      AND u.is_deleted = false;

    IF user_locked = p_password_hash THEN
        RETURN 'LOCKED';
    ELSEIF password_hash = p_password_hash THEN
        RETURN 'SUCCESS';
    ELSE
        RETURN 'FAILURE';
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Change user password
CREATE OR REPLACE PROCEDURE change_user_password(
    p_user_id INT,
    p_new_password_hash VARCHAR(255)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE users as u
    SET password_hash = p_new_password_hash,
        password_date = NOW()
    WHERE u.user_id = p_user_id
      AND u.is_deleted = false;
END;
$$;

CREATE OR REPLACE FUNCTION get_one_time_password(p_user_id INT)
    RETURNS TABLE
            (
                request_id    INT,
                user_id       INT,
                password_hash VARCHAR
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT otp.request_id,
               otp.user_id,
               otp.password_hash
        FROM one_time_passwords as otp
        WHERE otp.user_id = p_user_id
          AND otp.expiry_date > CURRENT_TIMESTAMP
          AND otp.remaining_attempts > 0;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE create_password_reset_request(p_user_id INT, p_password_hash VARCHAR, p_expiry_date TIMESTAMP)
    LANGUAGE plpgsql
AS
$$
BEGIN
    -- Insert the password reset request into the one_time_passwords table
    DELETE FROM one_time_passwords WHERE user_id = p_user_id;

    INSERT INTO one_time_passwords (user_id, password_hash, expiry_date)
    VALUES (p_user_id, p_password_hash, p_expiry_date);
END;
$$;

CREATE OR REPLACE PROCEDURE handle_wrong_otp(IN p_request_id INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE one_time_passwords
    SET remaining_attempts = remaining_attempts - 1
    WHERE request_id = p_request_id;

    IF (SELECT remaining_attempts FROM one_time_passwords WHERE request_id = p_request_id) = 0 THEN
        UPDATE one_time_passwords
        SET expiry_date = CURRENT_TIMESTAMP
        WHERE request_id = p_request_id;
    END IF;
END; $$
