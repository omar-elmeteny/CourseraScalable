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
