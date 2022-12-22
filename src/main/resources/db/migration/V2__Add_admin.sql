insert into usr (id, activation_code, active, email, password, username) values
    (1, null, true, 'test@test.ru', '$2a$08$eApn9x3qPiwp6cBVRYaDXed3J/usFEkcZbuc3FDa74bKOpUzHR.S.', 'admin'),
    (2, null, true, 'test@test.ru', '1234', 'administrator'),
    (3, null, true, 'test@test.ru', '12345', 'user');

insert into user_role (user_id, roles) values
    (1, 'ADMIN'),
    (2, 'ADMIN'),
    (3, 'USER');