CREATE TABLE course (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    instructor_id BIGINT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    rating DOUBLE PRECISION,
    duration INTERVAL,
    status VARCHAR(50) NOT NULL,
    categories TEXT[], 
    certificate_id UUID NOT NULL,
    constraint fk_instructor_id FOREIGN KEY (instructor_id) REFERENCES users(user_id),
    constraint status_check
        CHECK (status IN ('draft', 'published', 'inactive'))
);

CREATE TABLE course_section (
                                id BIGINT PRIMARY KEY,
                                course_id BIGINT NOT NULL,
                                title VARCHAR(255) NOT NULL,
                                description TEXT NOT NULL,
                                order_number INT NOT NULL,
                                FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE course_content (
    id BIGINT PRIMARY KEY,
    section_id BIGINT NOT NULL,
    duration INTERVAL NOT NULL,
    order_number INT NOT NULL,
    multimedia_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    constraint fk_section_id FOREIGN KEY (section_id) REFERENCES course_section(id),
    constraint type_check
        CHECK (type IN ('video', 'audio', 'text', 'image'))
);


CREATE TABLE course_enrollment (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    certificate_id UUID,
    FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE course_review (
    id BIGINT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    date TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE grade (
    id BIGINT PRIMARY KEY,
    enrollment_id BIGINT NOT NULL,
    grade DOUBLE PRECISION NOT NULL,
    content_id UUID NOT NULL,
    FOREIGN KEY (enrollment_id) REFERENCES course_enrollment(id)
);

CREATE TABLE progress (
    id BIGINT PRIMARY KEY,
    section_id BIGINT NOT NULL,
    enrollment_id BIGINT NOT NULL,
    progress DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (section_id) REFERENCES course_section(id),
    FOREIGN KEY (enrollment_id) REFERENCES course_enrollment(id)
);
