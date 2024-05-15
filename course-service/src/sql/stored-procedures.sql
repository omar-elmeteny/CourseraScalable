-- 1. course

--1.1 create course
CREATE OR REPLACE PROCEDURE create_course(
    IN course_id BIGINT,
    IN title VARCHAR(255),
    IN description TEXT,
    IN status VARCHAR(50)
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO course (id, title, description, status) VALUES (course_id, title, description, status);
END;
$$;


--1.2 get course
CREATE OR REPLACE PROCEDURE get_course(
    IN course_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course WHERE id = course_id;
END;
$$;

--1.3 update course
CREATE OR REPLACE PROCEDURE update_course(
    IN course_id BIGINT,
    IN title VARCHAR(255),
    IN description TEXT,
    IN status VARCHAR(50)
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE course SET title = title, description = description, status = status WHERE id = course_id;
END;
$$;

--1.4 delete course
CREATE OR REPLACE PROCEDURE delete_course(
    IN course_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM course WHERE id = course_id;
END;
$$;

--1.5 get all courses
CREATE OR REPLACE PROCEDURE get_all_courses() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course;
END;
$$;

--2. course_content

--2.1 create course content
CREATE OR REPLACE PROCEDURE create_course_content(
    IN content_id BIGINT,
    IN section_id BIGINT,
    IN duration INTERVAL,
    IN order_number INT,
    IN multimedia_id UUID,
    IN type VARCHAR(50)
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO course_content (id, section_id, duration, order_number, multimedia_id, type) VALUES (content_id, section_id, duration, order_number, multimedia_id, type);
END;
$$;

--2.2 get course content
CREATE OR REPLACE PROCEDURE get_course_content(
    IN content_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_content WHERE id = content_id;
END;
$$;

--2.3 update course content
CREATE OR REPLACE PROCEDURE update_course_content(
    IN content_id BIGINT,
    IN section_id BIGINT,
    IN duration INTERVAL,
    IN order_number INT,
    IN multimedia_id UUID,
    IN type VARCHAR(50)
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE course_content SET section_id = section_id, duration = duration, order_number = order_number, multimedia_id = multimedia_id, type = type WHERE id = content_id;
END;
$$;

--2.4 delete course content
CREATE OR REPLACE PROCEDURE delete_course_content(
    IN content_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM course_content WHERE id = content_id;
END;
$$;

--2.5 get all course content
CREATE OR REPLACE PROCEDURE get_all_course_content() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_content;
END;
$$;

--3. course_enrollment

--3.1 create course enrollment
CREATE OR REPLACE PROCEDURE create_course_enrollment(
    IN enrollment_id BIGINT,
    IN user_id BIGINT,
    IN course_id BIGINT,
    IN certificate_id UUID
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO course_enrollment (id, user_id, course_id, certificate_id) VALUES (enrollment_id, user_id, course_id, certificate_id);
END;
$$;

--3.2 get course enrollment
CREATE OR REPLACE PROCEDURE get_course_enrollment(
    IN enrollment_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_enrollment WHERE id = enrollment_id;
END;
$$;

--3.3 update course enrollment
CREATE OR REPLACE PROCEDURE update_course_enrollment(
    IN enrollment_id BIGINT,
    IN user_id BIGINT,
    IN course_id BIGINT,
    IN certificate_id UUID
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE course_enrollment SET user_id = user_id, course_id = course_id, certificate_id = certificate_id WHERE id = enrollment_id;
END;
$$;

--3.4 delete course enrollment
CREATE OR REPLACE PROCEDURE delete_course_enrollment(
    IN enrollment_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM course_enrollment WHERE id = enrollment_id;
END;
$$;

--3.5 get all course enrollments
CREATE OR REPLACE PROCEDURE get_all_course_enrollments() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_enrollment;
END;
$$;

--4. course_review

--4.1 create course review

CREATE OR REPLACE PROCEDURE create_course_review(
    IN review_id BIGINT,
    IN course_id BIGINT,
    IN user_id BIGINT,
    IN comment TEXT,
    IN date TIMESTAMP
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO course_review (id, course_id, user_id, comment, date) VALUES (review_id, course_id, user_id, comment, date);
END;
$$;

--4.2 get course review
CREATE OR REPLACE PROCEDURE get_course_review(
    IN review_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_review WHERE id = review_id;
END;
$$;

--4.3 update course review
CREATE OR REPLACE PROCEDURE update_course_review(
    IN review_id BIGINT,
    IN course_id BIGINT,
    IN user_id BIGINT,
    IN comment TEXT,
    IN date TIMESTAMP
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE course_review SET course_id = course_id, user_id = user_id, comment = comment, date = date WHERE id = review_id;
END;
$$;

--4.4 delete course review
CREATE OR REPLACE PROCEDURE delete_course_review(
    IN review_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM course_review WHERE id = review_id;
END;
$$;

--4.5 get all course reviews
CREATE OR REPLACE PROCEDURE get_all_course_reviews() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_review;
END;
$$;

--5. course_section

--5.1 create course section
CREATE OR REPLACE PROCEDURE create_course_section(
    IN section_id BIGINT,
    IN course_id BIGINT,
    IN title VARCHAR(255),
    IN description TEXT,
    IN order_number INT
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO course_section (id, course_id, title, description, order_number) VALUES (section_id, course_id, title, description, order_number);
END;
$$;

--5.2 get course section
CREATE OR REPLACE PROCEDURE get_course_section(
    IN section_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_section WHERE id = section_id;
END;
$$;

--5.3 update course section
CREATE OR REPLACE PROCEDURE update_course_section(
    IN section_id BIGINT,
    IN course_id BIGINT,
    IN title VARCHAR(255),
    IN description TEXT,
    IN order_number INT
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE course_section SET course_id = course_id, title = title, description = description, order_number = order_number WHERE id = section_id;
END;
$$;

--5.4 delete course section
CREATE OR REPLACE PROCEDURE delete_course_section(
    IN section_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM course_section WHERE id = section_id;
END;
$$;

--5.5 get all course sections
CREATE OR REPLACE PROCEDURE get_all_course_sections() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM course_section;
END;
$$;

--6. grade

--6.1 create grade
CREATE OR REPLACE PROCEDURE create_grade(
    IN grade_id BIGINT,
    IN enrollment_id BIGINT,
    IN grade DOUBLE PRECISION,
    IN content_id UUID
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO grade (id, enrollment_id, grade, content_id) VALUES (grade_id, enrollment_id, grade, content_id);
END;
$$;

--6.2 get grade
CREATE OR REPLACE PROCEDURE get_grade(
    IN grade_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM grade WHERE id = grade_id;
END;
$$;

--6.3 update grade
CREATE OR REPLACE PROCEDURE update_grade(
    IN grade_id BIGINT,
    IN enrollment_id BIGINT,
    IN grade DOUBLE PRECISION,
    IN content_id UUID
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE grade SET enrollment_id = enrollment_id, grade = grade, content_id = content_id WHERE id = grade_id;
END;
$$;

--6.4 delete grade
CREATE OR REPLACE PROCEDURE delete_grade(
    IN grade_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM grade WHERE id = grade_id;
END;
$$;

--6.5 get all grades
CREATE OR REPLACE PROCEDURE get_all_grades() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM grade;
END;
$$;

--7. progress

--7.1 create progress
CREATE OR REPLACE PROCEDURE create_progress(
    IN progress_id BIGINT,
    IN section_id BIGINT,
    IN enrollment_id BIGINT,
    IN progress DOUBLE PRECISION
) LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO progress (id, section_id, enrollment_id, progress) VALUES (progress_id, section_id, enrollment_id, progress);
END;
$$;

--7.2 get progress
CREATE OR REPLACE PROCEDURE get_progress(
    IN progress_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM progress WHERE id = progress_id;
END;
$$;

--7.3 update progress
CREATE OR REPLACE PROCEDURE update_progress(
    IN progress_id BIGINT,
    IN section_id BIGINT,
    IN enrollment_id BIGINT,
    IN progress DOUBLE PRECISION
) LANGUAGE plpgsql AS $$
BEGIN
    UPDATE progress SET section_id = section_id, enrollment_id = enrollment_id, progress = progress WHERE id = progress_id;
END;
$$;

--7.4 delete progress
CREATE OR REPLACE PROCEDURE delete_progress(
    IN progress_id BIGINT
) LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM progress WHERE id = progress_id;
END;
$$;

--7.5 get all progress
CREATE OR REPLACE PROCEDURE get_all_progress() LANGUAGE plpgsql AS $$
BEGIN
    SELECT * FROM progress;
END;
$$;
