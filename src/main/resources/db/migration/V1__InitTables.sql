CREATE TABLE Author (
  id SERIAL PRIMARY KEY,

  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL,
  username VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX ON Author (email);

CREATE TABLE Post (
  id SERIAL PRIMARY KEY,

  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  published_on TIMESTAMP NOT NULL,
  updated_on TIMESTAMP,
  author_id INT,

  FOREIGN key (author_id) REFERENCES Author (id)
);
CREATE UNIQUE INDEX ON Post (title);

CREATE TABLE Comment (
  id SERIAL PRIMARY KEY,

  post_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  content TEXT NOT NULL,
  published_on TIMESTAMP NOT NULL,
  updated_on TIMESTAMP,

  FOREIGN key (post_id) REFERENCES Post (id)
);