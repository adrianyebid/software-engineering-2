CREATE TABLE IF NOT EXISTS training_types (
    id SERIAL PRIMARY KEY,
    training_type_name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO training_types (id, training_type_name) VALUES
(1, 'Cardio'),
(2, 'Strength'),
(3, 'Flexibility'),
(4, 'HIIT'),
(5, 'Yoga')
ON CONFLICT (id) DO NOTHING;