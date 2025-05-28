---- Вставка данных в таблицу TRAINER
--INSERT INTO TRAINER (name, surname, birthday, telephone, email, login) VALUES
--('Иван', 'Иванов', '1985-05-15', '+79001234567', 'ivan.ivanov@example.com', 'ivan123'),
--('Ткачук', 'Алексей', '1989-10-17', '+79658436975', 'box03-853@yandex.ru', 'box03');
--
---- Вставка данных в таблицу CLIENT
--INSERT INTO CLIENT (name, surname, birthday, telephone, email, login) VALUES
--('Петр', 'Петров', '1990-01-10', '+79007654321', 'petr.petrov@example.com', 'petr123'),
--('Светлана', 'Сидорова', '1995-03-20', '+79009876543', 'svetlana.sidorova@example.com', 'svetlana123');
--
----Вставка данных в таблицу WORKOUT
--INSERT INTO WORKOUT (client_id, trainer_id, "date", start_time, end_time) VALUES
--(1, 1, '2023-10-01', '10:00:00', '11:00:00');

INSERT INTO TRAINER (name, surname, birthday, telephone, email, login) VALUES
('Алексей', 'Иванов', '1980-05-15', '+79161234567', 'alexey.ivanov@example.com', 'alexey80'),
('Марина', 'Кузнецова', '1985-09-20', '+79169876543', 'marina.kuznetsova@example.com', 'marina85');

INSERT INTO CLIENT (name, surname, birthday, telephone, email, login) VALUES
('Иван', 'Петров', '1990-01-10', '+79007654321', 'ivan.petrov@example.com', 'ivan90'),
('Светлана', 'Сидорова', '1995-03-20', '+79009876543', 'svetlana.sidorova@example.com', 'svetlana95'),
('Дмитрий', 'Козлов', '1988-07-12', '+79005551234', 'dmitry.kozlov@example.com', 'dmitry88'),
('Ольга', 'Морозова', '1992-11-05', '+79002223344', 'olga.morozova@example.com', 'olga92'),
('Елена', 'Новикова', '1994-06-30', '+79003334455', 'elena.novikova@example.com', 'elena94');


INSERT INTO WORKOUT (client_id, trainer_id, "date", start_time, end_time) VALUES
(1, 1, '2024-06-01', '09:00:00', '10:00:00'),
(2, 1, '2024-06-02', '11:00:00', '12:00:00'),
(3, 2, '2024-06-01', '10:00:00', '11:00:00'),
(4, 2, '2024-06-03', '14:00:00', '15:00:00'),
(5, 2, '2024-06-04', '16:00:00', '17:00:00');