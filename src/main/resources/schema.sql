
CREATE TABLE IF NOT EXISTS rating_mpa
(
    id   INT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
    id   INT PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS director
(
    id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS film
(
    id            INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(64) NOT NULL,
    description   VARCHAR(200),
    release_date  DATE,
    duration      INT,
    rating_mpa_id INT REFERENCES rating_mpa
);

CREATE TABLE IF NOT EXISTS film_directors
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id     INT NOT NULL REFERENCES film (id) ON DELETE CASCADE,
    director_id INT NOT NULL REFERENCES director (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS film_directors_uq1 on film_directors (film_id, director_id);

CREATE TABLE IF NOT EXISTS film_genre
(
    id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id  INT REFERENCES film ON DELETE CASCADE,
    genre_id INT REFERENCES genre
);

CREATE UNIQUE INDEX IF NOT EXISTS film_genre_uq1 on film_genre (film_id, genre_id);

CREATE TABLE IF NOT EXISTS user_
(
    id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    name     VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS like_
(
    id      INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id INT REFERENCES film ON DELETE CASCADE,
    user_id INT REFERENCES user_ ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS like_uq1 on like_ (film_id, user_id);

CREATE TABLE IF NOT EXISTS friend_request
(
    id                INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_initiator_id INT REFERENCES user_ ON DELETE CASCADE,
    user_friend_id    INT REFERENCES user_ ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS friend_request_uq1 on friend_request (user_initiator_id, user_friend_id);

CREATE TABLE IF NOT EXISTS reviews (
    review_id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id         INT REFERENCES user_ ON DELETE CASCADE NOT NULL,
    film_id         INT REFERENCES film ON DELETE CASCADE NOT NULL,
    is_positive     BOOLEAN NOT NULL,
    useful          INT NOT NULL,
    content         VARCHAR(128)
);

CREATE UNIQUE INDEX IF NOT EXISTS reviews_uq_user_film on reviews (user_id, film_id);

CREATE TABLE IF NOT EXISTS review_likes (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id         INT REFERENCES user_ ON DELETE CASCADE NOT NULL,
    review_id       INT REFERENCES reviews ON DELETE CASCADE NOT NULL,
    is_like         BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS review_likes_uq_user_review on review_likes (user_id, review_id);

