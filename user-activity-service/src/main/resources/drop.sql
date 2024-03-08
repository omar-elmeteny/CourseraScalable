DROP FUNCTION IF EXISTS insert_user_activity_log(INT, VARCHAR(50), TEXT);

DROP FUNCTION IF EXISTS get_user_activity_logs(INT, INT, INT);

DROP FUNCTION IF EXISTS delete_user_activity_log(INT);

DROP FUNCTION IF EXISTS find_by_user_id_ordered_by_activity_date_desc(INT, INT, INT);
