DO $$ DECLARE
r RECORD;
BEGIN
    -- drop tables
FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
END LOOP;

    -- drop sequences
FOR r IN (SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = current_schema()) LOOP
        EXECUTE 'DROP SEQUENCE IF EXISTS ' || quote_ident(r.sequence_name) || ' CASCADE';
END LOOP;

    -- drop views
FOR r IN (SELECT table_name FROM information_schema.views WHERE table_schema = current_schema()) LOOP
        EXECUTE 'DROP VIEW IF EXISTS ' || quote_ident(r.table_name) || ' CASCADE';
END LOOP;

    -- drop functions
FOR r IN (SELECT routine_name FROM information_schema.routines WHERE specific_schema = current_schema()) LOOP
        EXECUTE 'DROP FUNCTION IF EXISTS ' || quote_ident(r.routine_name) || ' CASCADE';
END LOOP;
END $$;