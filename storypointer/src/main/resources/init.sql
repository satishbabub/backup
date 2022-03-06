CREATE TABLE SESSION(
   -- id INT NOT NULL,
    created_on DATETIME NOT NULL,
    is_active BOOLEAN,
    created_by VARCHAR(36),
    team VARCHAR(16),
    PRIMARY KEY (team));

CREATE TABLE SESSION_USER(
    id INT,
    session_id VARCHAR(16),
    user_name VARCHAR(36),
    user_role VARCHAR (12),
    PRIMARY KEY (id));

CREATE TABLE USER_STORY_POINTS(
    id INT,
    story_id VARCHAR(12),
    user_name VARCHAR(36),
    team VARCHAR(16),
    estimate SMALLINT,
    PRIMARY KEY (id));

CREATE TABLE STORY_POINTS
(
    story_id  VARCHAR(12),
    is_active BOOLEAN,
    estimate SMALLINT,
    team VARCHAR(16),
    PRIMARY KEY (story_id));

--ALTER TABLE SESSION_USER ADD CONSTRAINT SESSION_USER_UNIQUE UNIQUE(session_id, user_name, user_role);

--ALTER TABLE SESSION_USER ADD FOREIGN KEY (id) REFERENCES SESSION(id));

--ALTER TABLE STORY_POINTS ADD CONSTRAINT STORY_POINTS_UNIQUE UNIQUE(story_id, user_name, points);

--ALTER TABLE STORY_POINTS ADD FOREIGN KEY (user_name) REFERENCES SESSION_USER(user_name))