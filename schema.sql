CREATE TABLE IF NOT EXISTS categories (
    id   SERIAL PRIMARY KEY,
    nume VARCHAR(255) UNIQUE NOT NULL,
    descriere TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id           SERIAL PRIMARY KEY,
    nume         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) UNIQUE NOT NULL,
    parola       VARCHAR(255) NOT NULL,
    rol          VARCHAR(50)  NOT NULL,
    specializare VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS courses (
    id           SERIAL PRIMARY KEY,
    titlu        VARCHAR(255)     NOT NULL,
    descriere    TEXT,
    profesor_id  INT              NOT NULL REFERENCES users(id),
    pret         DOUBLE PRECISION NOT NULL,
    categorie_id INT              NOT NULL REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS enrollments (
    id              SERIAL PRIMARY KEY,
    student_id      INT  NOT NULL REFERENCES users(id),
    curs_id         INT  NOT NULL REFERENCES courses(id),
    data_inscriere  DATE NOT NULL,
    UNIQUE (student_id, curs_id)
);

CREATE TABLE IF NOT EXISTS quizzes (
    id             SERIAL PRIMARY KEY,
    titlu          VARCHAR(255) NOT NULL,
    punctaj_minim  INT          NOT NULL,
    curs_id        INT          NOT NULL REFERENCES courses(id)
);
