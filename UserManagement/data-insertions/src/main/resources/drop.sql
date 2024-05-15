DO $$ DECLARE
    r RECORD;
BEGIN

    -- drop functions
    FOR r IN (SELECT p.proname AS name, CASE WHEN p.prokind = 'f' THEN 'FUNCTION' ELSE 'PROCEDURE' END AS type
              FROM pg_proc p
                       JOIN pg_namespace n ON p.pronamespace = n.oid
              WHERE n.nspname = current_schema()) LOOP
            EXECUTE 'DROP ' || r.type ||  ' IF EXISTS ' || quote_ident(r.name);
        END LOOP;


    -- drop tables
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
            EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
        END LOOP;
END $$;