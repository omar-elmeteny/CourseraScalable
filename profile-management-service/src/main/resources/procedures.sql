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
    bio TEXT,
    profile_photo_url VARCHAR(255),
    phone_number VARCHAR(15),
    date_of_birth VARCHAR(10)  -- Change type to VARCHAR for date_of_birth
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        up.profile_id::INTEGER,
        up.user_id::INTEGER,
        up.first_name,
        up.last_name,
        up.is_email_verified,
        up.is_phone_verified,
        up.bio,
        up.profile_photo_url,
        up.phone_number,
        CASE
            WHEN up.date_of_birth IS NOT NULL THEN CAST(up.date_of_birth AS VARCHAR(10))
            ELSE NULL
        END AS date_of_birth
    FROM user_profile AS up
    WHERE
        (p_user_id IS NULL OR up.user_id = p_user_id) AND
        (p_first_name IS NULL OR up.first_name = p_first_name) AND
        (p_last_name IS NULL OR up.last_name = p_last_name) AND
        (p_is_email_verified IS NULL OR up.is_email_verified = p_is_email_verified) AND
        (p_is_phone_verified IS NULL OR up.is_phone_verified = p_is_phone_verified) AND
        (p_phone_number IS NULL OR up.phone_number = p_phone_number);
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION find_user_profile_by_user_id(
    p_user_id INT
) RETURNS TABLE (
    profile_id INT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_email_verified BOOLEAN,
    is_phone_verified BOOLEAN,
    bio TEXT,
    profile_photo_url VARCHAR(255),
    phone_number VARCHAR(15),
    date_of_birth VARCHAR(10)  -- Change type to VARCHAR for date_of_birth
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        up.profile_id::INTEGER,
        up.user_id::INTEGER,
        up.first_name,
        up.last_name,
        up.is_email_verified,
        up.is_phone_verified,
        up.bio,
        up.profile_photo_url,
        up.phone_number,
        CASE
            WHEN up.date_of_birth IS NOT NULL THEN CAST(up.date_of_birth AS VARCHAR(10))
            ELSE NULL
        END AS date_of_birth
    FROM user_profile AS up
    WHERE
        up.user_id = p_user_id ;
END;
$$ LANGUAGE plpgsql;




CREATE OR REPLACE FUNCTION find_user_profile_by_profile_id(
    p_profile_id INT
) RETURNS TABLE (
    profile_id INT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_email_verified BOOLEAN,
    is_phone_verified BOOLEAN,
    bio TEXT,
    profile_photo_url VARCHAR(255),
    phone_number VARCHAR(15),
    date_of_birth VARCHAR(10)  -- Change type to VARCHAR for date_of_birth
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        up.profile_id::INTEGER,
        up.user_id::INTEGER,
        up.first_name,
        up.last_name,
        up.is_email_verified,
        up.is_phone_verified,
        up.bio,
        up.profile_photo_url,
        up.phone_number,
        CASE
            WHEN up.date_of_birth IS NOT NULL THEN CAST(up.date_of_birth AS VARCHAR(10))
            ELSE NULL
        END AS date_of_birth
    FROM user_profile AS up
    WHERE
        up.profile_id = p_profile_id ;
END;
$$ LANGUAGE plpgsql;
