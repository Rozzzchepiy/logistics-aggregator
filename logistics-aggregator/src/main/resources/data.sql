INSERT INTO users (name, email, password, number, photo) VALUES
                                                             ('Юрій', 'customer@example.com', 'hashed_pass_1', '+380671112233', null),
                                                             ('Олег', 'driver@example.com', 'hashed_pass_2', '+380501112233', null),
                                                             ('Іван', 'loader@example.com', 'hashed_pass_3', '+380931112233', null),
                                                             ('Максим', 'max@example.com', 'hashed_pass_4', '+380631112233', null);

INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'CUSTOMER'),
                                           (2, 'DRIVER'),
                                           (3, 'LOADER'),
                                           (4, 'CUSTOMER'),
                                           (4, 'DRIVER');

INSERT INTO customer_profiles (user_id, customer_rating) VALUES
                                                             (1, 5.00),
                                                             (4, 4.80);

INSERT INTO driver_profiles (user_id, age, car_brand, car_volume, driver_rating) VALUES
                                                                                     (2, 35, 'Mercedes Sprinter', 12.50, 4.90),
                                                                                     (4, 28, 'Renault Master', 10.00, 5.00);

INSERT INTO loader_profiles (user_id, age, height, weight, loader_rating) VALUES
    (3, 24, 185, 90.50, 4.95);

INSERT INTO orders (user_id, status, total_price) VALUES
                                                      (1, 'ACCEPTED', 2500.00),
                                                      (4, 'IN_PROGRESS', 1200.50);