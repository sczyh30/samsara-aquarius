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
  time DATETIME NOT NULL,
  CONSTRAINT fk_cm_dataid FOREIGN KEY (data_id) REFERENCES info_data (id),
  CONSTRAINT fk_cm_uid FOREIGN KEY (uid) REFERENCES user (uid)
);
CREATE INDEX fk_cm_dataid_idx ON comment (data_id);
CREATE INDEX fk_cm_uid_idx ON comment (uid);

CREATE TABLE info_data
(
  id INT(11) PRIMARY KEY NOT NULL,
  title VARCHAR(85) DEFAULT 'No Title!' NOT NULL,
  url VARCHAR(150) NOT NULL,
  cid INT(11) NOT NULL,
  update_date DATE NOT NULL
);

CREATE TABLE user
(
  uid INT(11) PRIMARY KEY NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(90) NOT NULL,
  join_date DATE NOT NULL,
  avatar VARCHAR(65) DEFAULT 'default',
  tips VARCHAR(100) DEFAULT 'None~',
  website VARCHAR(65) NOT NULL
);
CREATE UNIQUE INDEX username_UNIQUE ON user (username);