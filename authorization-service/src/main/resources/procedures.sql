
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
    out exist bool
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