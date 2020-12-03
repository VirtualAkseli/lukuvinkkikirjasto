CREATE TABLE IF NOT EXISTS Tips
(
    id              IDENTITY,
    tip_type        TEXT                NOT NULL CHECK tip_type IN ('book', 'video', 'blogpost', 'podcast'),
    author          TEXT,
    tip_name        TEXT,
    identifier      TEXT,               /* For example ISBN, ISRC, DOI etc. */
    url             TEXT,
    comments        TEXT
);

CREATE TABLE IF NOT EXISTS Courses
(
    id              IDENTITY,
    course_name     TEXT                NOT NULL
);

CREATE TABLE IF NOT EXISTS Tags
(
    id              IDENTITY,
    tag_name        TEXT                NOT NULL
);

CREATE TABLE IF NOT EXISTS Tip_courses
(
    tip_id      BIGINT                  NOT NULL,
    course_id   BIGINT                  NOT NULL,

    FOREIGN KEY (tip_id) REFERENCES Tips (id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES Courses (id)
);

CREATE TABLE IF NOT EXISTS Tip_tags
(
    tip_id      BIGINT                  NOT NULL,
    tag_id      BIGINT                  NOT NULL,

    FOREIGN KEY (tip_id) REFERENCES Tips (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags (id)
);