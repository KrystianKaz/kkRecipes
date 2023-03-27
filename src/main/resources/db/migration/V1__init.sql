DROP TABLE IF EXISTS daily_diet, user, meal;

CREATE TABLE daily_diet
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    date            DATE,
    added_by_user   BIGINT NOT NULL,
    calories        DOUBLE,
    fat             DOUBLE NOT NULL,
    carbohydrates   DOUBLE,
    protein         DOUBLE NOT NULL,
    description     VARCHAR(255),
    diet_type       VARCHAR(255),
    first_meal_id   INT NOT NULL,
    second_meal_id  INT NOT NULL,
    third_meal_id   INT NOT NULL,
    completed       BIT
);

CREATE TABLE user
(
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_creation_date       DATE,
    account_creation_time       TIME,
    active                      BIT NOT NULL,
    email                       VARCHAR(30) NOT NULL UNIQUE,
    password                    VARCHAR(60) NOT NULL,
    user_roles                  ENUM('ADMIN', 'MODERATOR', 'USER'),
    username                    VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE meal
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    meal_id         INT NOT NULL,
    meal_title      VARCHAR(255),
    still_liked     BIT NOT NULL,
    liked_by_user   BIGINT
);
