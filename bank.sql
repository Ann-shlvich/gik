DROP DATABASE IF EXISTS `Bank`;
CREATE DATABASE `Bank` DEFAULT CHARACTER SET utf8;
USE Bank;

CREATE TABLE User (
    UserId INTEGER NOT NULL AUTO_INCREMENT,
    FullName VARCHAR(40) NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    Login VARCHAR(20) NOT NULL,
    Password VARCHAR(32) NOT NULL,
    Role VARCHAR(10) NOT NULL,
    PRIMARY KEY (UserId)
);

CREATE TABLE Examination (
    ExaminationId INTEGER NOT NULL AUTO_INCREMENT,
    Title VARCHAR(50) NOT NULL,
    Description VARCHAR(500) NOT NULL DEFAULT '',
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    PRIMARY KEY (ExaminationId)
);

CREATE TABLE Alternative (
    AlternativeId INTEGER NOT NULL AUTO_INCREMENT,
    Ordinal INTEGER NOT NULL,
    Description VARCHAR(100) NOT NULL,
    ExaminationId INTEGER NOT NULL,
    PRIMARY KEY (AlternativeId),
    KEY `Alternative_Examination_idx` (ExaminationId),
    CONSTRAINT `Alternative_Examination_idx` FOREIGN KEY (ExaminationId) REFERENCES Examination (ExaminationId)
);

CREATE TABLE Opinion(
    OpinionId INTEGER NOT NULL AUTO_INCREMENT,
    Datum BLOB NOT NULL,
    ExaminationId INTEGER NOT NULL,
    UserId INTEGER NOT NULL,
    PRIMARY KEY (OpinionId),
    KEY `Opinion_Examination_idx` (ExaminationId),
    KEY `Opinion_User_idx` (UserId),
    CONSTRAINT `Opinion_Examination` FOREIGN KEY (ExaminationId) REFERENCES Examination (ExaminationId),
    CONSTRAINT `Opinion_User` FOREIGN KEY (UserId) REFERENCES User (UserId)
);

INSERT INTO User (UserId, FullName, Phone, Login, Password, Role) VALUES
    (1, 'Попов Петр Иванович', '+375-29-213-45-67', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN'),
    (2, 'Новиков Сергей Михайлович', '+375-33-546-67-89', 'sergey', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (3, 'Федорова Инна Владимировна', '+375-44-564-89-12', 'inna', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (4, 'Гришина Елена Сергеевна', '+375-29-125-76-89', 'elena', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (5, 'Кубарев Дмитрий Семенович', '+375-33-565-56-43', 'dmitri', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (6, 'Морозова Юлия Дмитриевна', '+375-44-554-12-47', 'ulia', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (7, 'Томилин Егор Владимирович', '+375-29-987-54-21', 'egor', '202cb962ac59075b964b07152d234b70', 'EXPERT'),
    (8, 'Локшина Ольга Викторовна', '+375-33-675-43-23', 'olga', '202cb962ac59075b964b07152d234b70', 'USER');

INSERT INTO Examination (ExaminationId, Title, Description, StartDate, EndDate) VALUES
    (1, 'Тестовая экспертиза 1', 'Описание тестовой экспертизы 1', '2021-05-01', '2021-05-31'),
    (2, 'Тестовая экспертиза 2', 'Описание тестовой экспертизы 2', '2021-05-01', '2021-05-31'),
    (3, 'Повышение квалификации', 'Руководство предприятия решило повысить квалификацию своих работников одним из следующих способов.', '2021-05-01', '2021-05-31');

INSERT INTO Alternative (AlternativeId, Ordinal, Description, ExaminationId) VALUES
(1, 1, 'Альтернатива 1.1', 1),
(2, 2, 'Альтернатива 1.2', 1),
(3, 3, 'Альтернатива 1.3', 1),
(4, 4, 'Альтернатива 1.4', 1),
(5, 1, 'Альтернатива 2.1', 2),
(6, 2, 'Альтернатива 2.2', 2),
(7, 3, 'Альтернатива 2.3', 2),
(8, 4, 'Альтернатива 2.4', 2),
( 9, 1, 'Направить работников на курсы повышения квалификации', 3),
(10, 2, 'Пригласить высококвалифицированного специалиста для прочтения лекций', 3),
(11, 3, 'Обеспечить работников необходимой литературой', 3);