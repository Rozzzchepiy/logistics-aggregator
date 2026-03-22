DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS driver_profiles CASCADE;
DROP TABLE IF EXISTS loader_profiles CASCADE;
DROP TABLE IF EXISTS customer_profiles CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       number VARCHAR(20) NOT NULL,
                       photo TEXT
);

CREATE TABLE user_roles (
                            user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            role VARCHAR(50) NOT NULL,
                            PRIMARY KEY (user_id, role)
);

CREATE TABLE customer_profiles (
                                   id BIGSERIAL PRIMARY KEY,
                                   user_id BIGINT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                                   customer_rating DECIMAL(3, 2) DEFAULT 5.0
);

CREATE TABLE driver_profiles (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                                 age INT,
                                 car_brand VARCHAR(100),
                                 car_volume DECIMAL(5, 2),
                                 driver_rating DECIMAL(3, 2) DEFAULT 5.0
);

CREATE TABLE loader_profiles (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                                 age INT,
                                 height INT,
                                 weight DECIMAL(5, 2),
                                 loader_rating DECIMAL(3, 2) DEFAULT 5.0
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                        status VARCHAR(50) NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);