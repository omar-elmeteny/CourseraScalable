
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    profile_photo_url VARCHAR(255),
    phone_number VARCHAR(15) UNIQUE
);


