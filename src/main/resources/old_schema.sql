CREATE TABLE IF NOT EXISTS Books
(
    id              IDENTITY,
    type            VARCHAR(1000)        default 'Book',
    author          VARCHAR(1000),
    title           VARCHAR(1000),
    isbn            VARCHAR(1000),
    relatedCourses  VARCHAR(1000),
    tags            VARCHAR(1000),
    comments        VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS Videos
(
    id              IDENTITY,
    type            VARCHAR(1000)        default 'Video',
    title           VARCHAR(1000),
    url             VARCHAR(1000),
    relatedCourses  VARCHAR(1000),
    tags            VARCHAR(1000),
    comments        VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS Blogs
(
    id              IDENTITY,
    type            VARCHAR(1000)        default 'Blogpost',
    title           VARCHAR(1000),
    author          VARCHAR(1000),
    url             VARCHAR(1000),
    relatedCourses  VARCHAR(1000),
    tags            VARCHAR(1000),
    comments        VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS Podcasts
(
    id              IDENTITY,
    type            VARCHAR(1000)        default 'Podcast',
    author          VARCHAR(1000),
    podcastName     VARCHAR(1000),
    otsikko         VARCHAR(1000),
    description     VARCHAR(1000),
    relatedCourses  VARCHAR(1000),
    tags            VARCHAR(1000),
    comments        VARCHAR(1000)
);