CREATE TABLE IF NOT EXISTS Tips
(
    id              IDENTITY,
    tip_type        TEXT                CHECK tip_type IN ('book', 'video', 'blogpost', 'podcast'),
    author          TEXT,
    tip_name        TEXT,
    identifier      TEXT,               /* For example ISBN, ISRC, DOI etc. */
    url             TEXT,
    comments        TEXT
);