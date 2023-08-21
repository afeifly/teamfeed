CREATE TABLE IF NOT EXISTS users(id SERIAL PRIMARY KEY, username VARCHAR(50),
    nickname VARCHAR(50), icon_index integer, password VARCHAR(300), attack integer);
CREATE TABLE IF NOT EXISTS feeds(id SERIAL PRIMARY KEY, title VARCHAR(100), url VARCHAR(300), ts TIMESTAMP);
CREATE TABLE IF NOT EXISTS articles(id SERIAL PRIMARY KEY,title VARCHAR(200),auther VARCHAR(50),
    url VARCHAR(300), content VARCHAR(300000), ts TIMESTAMP,
    fk_f_id integer REFERENCES feeds (id));

CREATE TABLE IF NOT EXISTS articles_users_relative(
    id SERIAL PRIMARY KEY,
    fk_a_id integer REFERENCES articles (id),
    fk_u_id integer REFERENCES users (id),
    CONSTRAINT aurr_unique UNIQUE(fk_a_id, fk_u_id)
    );

CREATE TABLE IF NOT EXISTS articles_like(
    id SERIAL PRIMARY KEY,
    fk_a_id integer REFERENCES articles (id),
    fk_u_id integer REFERENCES users (id),
    CONSTRAINT alrr_unique UNIQUE(fk_a_id, fk_u_id)
    );

CREATE TABLE IF NOT EXISTS article_comments (
    id SERIAL PRIMARY KEY,
    content VARCHAR(1000),
    ts  TIMESTAMP,
    fk_a_id integer REFERENCES articles (id),
    fk_u_id integer REFERENCES users (id));
