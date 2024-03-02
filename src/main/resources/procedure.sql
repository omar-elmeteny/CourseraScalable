CREATE OR REPLACE FUNCTION insert_user(
    p_username VARCHAR(255),
    p_email VARCHAR(255),
    p_password_hash VARCHAR(255),
    p_full_name VARCHAR(50),
    p_date_of_birth DATE,
    p_phone_number VARCHAR(15),
    OUT result_message VARCHAR(255)
)
AS $$
BEGIN
    INSERT INTO users (username, email, password_hash, full_name, date_of_birth, phone_number)
    VALUES (p_username, p_email, p_password_hash, p_full_name, p_date_of_birth, p_phone_number);

    result_message := 'User inserted successfully.';
END;
$$ LANGUAGE plpgsql;
```



CREATE OR REPLACE FUNCTION check_user_exists(
    IN p_username VARCHAR(255),
    IN p_email VARCHAR(255),
    OUT user_exists BOOLEAN,
    OUT message VARCHAR(255)
)
AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM users WHERE username = p_username OR email = p_email) THEN
        user_exists := TRUE;
        message := 'User with the provided username or email already exists.';
    ELSE
        user_exists := FALSE;
        message := 'User does not exist.';
    END IF;
END;
$$ LANGUAGE plpgsql;
