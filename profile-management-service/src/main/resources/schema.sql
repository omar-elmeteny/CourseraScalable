
CREATE TABLE IF NOT EXISTS user_profile (
    profile_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bio TEXT,
    profile_photo_url VARCHAR(255),
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_number VARCHAR(15) UNIQUE,
    date_of_birth DATE NOT NULL,
);


