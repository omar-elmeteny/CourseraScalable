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
    activity_type VARCHAR(50),
    activity_description TEXT,
    activity_date TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        log_id,
        activity_type,
        activity_description,
        activity_date
    FROM user_activity_logs
    WHERE user_id = p_user_id
    ORDER BY activity_date DESC;
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
