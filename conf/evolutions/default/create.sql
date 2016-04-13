# samsara_aquarius schema

CREATE TABLE adm1n
(
  id INT(11) PRIMARY KEY NOT NULL,
  name VARCHAR(45) NOT NULL,
  password VARCHAR(110) NOT NULL,
  role INT(11) DEFAULT '1',
  email VARCHAR(65) NOT NULL
);
CREATE UNIQUE INDEX name_UNIQUE ON adm1n (name);
CREATE TABLE comment
(
  cid INT(11) PRIMARY KEY NOT NULL,
  uid INT(11) NOT NULL,
  data_id INT(11) NOT NULL,
  text VARCHAR(150) NOT NULL,
  time DATETIME NOT NULL
);
CREATE INDEX index_aid ON comment (data_id);
CREATE INDEX index_uid ON comment (uid);
CREATE INDEX index_u_a ON comment (uid, data_id);
CREATE TABLE info_data
(
  id INT(11) PRIMARY KEY NOT NULL,
  title VARCHAR(85) DEFAULT 'No Title!' NOT NULL,
  author VARCHAR(45) NOT NULL,
  url VARCHAR(150) NOT NULL,
  cid INT(11) NOT NULL,
  update_date DATE NOT NULL
);
CREATE INDEX index_cid ON info_data (cid);
CREATE INDEX index_title ON info_data (title);
CREATE TABLE user
(
  uid INT(11) PRIMARY KEY NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(90) NOT NULL,
  join_date DATE NOT NULL,
  avatar VARCHAR(65) DEFAULT 'default.png',
  tips VARCHAR(100) DEFAULT '无',
  website VARCHAR(65) DEFAULT '',
  email VARCHAR(60) NOT NULL
);
CREATE UNIQUE INDEX username_UNIQUE ON user (username);
CREATE TABLE category
(
  cid INT(11) PRIMARY KEY NOT NULL,
  name VARCHAR(45) NOT NULL,
  abbr VARCHAR(45) NOT NULL
);
CREATE UNIQUE INDEX abbr_UNIQUE ON category (abbr);
CREATE UNIQUE INDEX name_UNIQUE ON category (name);
CREATE TABLE share_pending
(
  sid INT(11) PRIMARY KEY NOT NULL,
  title VARCHAR(85) NOT NULL,
  url VARCHAR(150) NOT NULL,
  user INT(11) DEFAULT '0'
);
CREATE INDEX index_title ON share_pending (title);
CREATE TABLE favorite
(
  article_id INT(11) NOT NULL,
  like_uid INT(11) NOT NULL,
  ctime TIMESTAMP(6),
  CONSTRAINT `PRIMARY` PRIMARY KEY (article_id, like_uid)
);
CREATE INDEX index_uid ON favorite (like_uid);