-- Вставка данных в таблицу TRAINER
INSERT INTO TRAINER (name, surname, birthday, telephone, email, login) VALUES
('Иван', 'Иванов', '1985-05-15', '+79001234567', 'ivan.ivanov@example.com', 'ivan123'),
('Ткачук', 'Алексей', '1989-10-17', '+79658436975', 'box03-853@yandex.ru', 'box03');

-- Вставка данных в таблицу CLIENT
INSERT INTO CLIENT (name, surname, birthday, telephone, email, login) VALUES
('Петр', 'Петров', '1990-01-10', '+79007654321', 'petr.petrov@example.com', 'petr123'),
('Светлана', 'Сидорова', '1995-03-20', '+79009876543', 'svetlana.sidorova@example.com', 'svetlana123');

--Вставка данных в таблицу WORKOUT
INSERT INTO WORKOUT (client_id, trainer_id, "date", start_time, end_time) VALUES
(1, 1, '2023-10-01', '10:00:00', '11:00:00');