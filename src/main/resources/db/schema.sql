-- 校园活动管理平台数据库结构
-- 建议数据库统一使用 utf8mb4 编码

CREATE DATABASE IF NOT EXISTS `campus`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `campus`;

CREATE TABLE IF NOT EXISTS admin_user (
    user_id     INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(64)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(32)  NOT NULL DEFAULT 'ADMIN',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS activity (
    activity_id      INT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(255) NOT NULL,
    type             VARCHAR(64),
    description      TEXT,
    start_time       DATETIME     NOT NULL,
    end_time         DATETIME     NOT NULL,
    location         VARCHAR(255),
    signup_end_time  DATETIME     NOT NULL,
    max_participants INT,
    status           VARCHAR(32)  NOT NULL DEFAULT 'DRAFT',
    created_by       INT,
    created_time     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    live_url         VARCHAR(255),
    attachment_url   VARCHAR(255),
    tags             VARCHAR(255),
    CONSTRAINT fk_activity_creator FOREIGN KEY (created_by) REFERENCES admin_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS registration (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    activity_id     INT          NOT NULL,
    name            VARCHAR(64)  NOT NULL,
    phone           VARCHAR(32)  NOT NULL,
    school          VARCHAR(128),
    college         VARCHAR(128),
    clazz           VARCHAR(128),
    student_no      VARCHAR(64)  NOT NULL,
    email           VARCHAR(128),
    created_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registration_activity FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    CONSTRAINT uq_registration_phone UNIQUE (activity_id, phone),
    CONSTRAINT uq_registration_student UNIQUE (activity_id, student_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS checkin (
    checkin_id   INT PRIMARY KEY AUTO_INCREMENT,
    activity_id  INT          NOT NULL,
    student_no   VARCHAR(100) NOT NULL,
    name         VARCHAR(100),
    phone        VARCHAR(50),
    checkin_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    method       VARCHAR(50),
    CONSTRAINT fk_checkin_activity FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    CONSTRAINT uq_checkin UNIQUE (activity_id, student_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
