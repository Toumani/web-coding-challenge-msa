CREATE DATABASE SHOPZZ

CREATE TABLE Shop (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  latitude NUMERIC(18,15) NOT NULL,
  longitude NUMERIC(18,15) NOT NULL,
  image VARCHAR(255)
) Engine=InnoDB;

CREATE TABLE User (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR (55) UNIQUE NOT NULL,
  password CHAR(40) NOT NULL
) Engine=InnoDB;

CREATE TABLE User_Shop (
  user_id INT NOT NULL,
  shop_id INT NOT NULL,
  interaction BOOLEAN NULL,
  date DATETIME NOT NULL,

  PRIMARY KEY (user_id, shop_id)
) Engine=InnoDB;

CREATE TABLE Connection (
  checksum CHAR(40) NOT NULL PRIMARY KEY, -- an arbitrary string used to identify request
  user_id INT NOT NULL,
  latitude NUMERIC(18,15) NOT NULL,
  longitude NUMERIC(18, 15) NOT NULL
) Engine=InnoDB;

ALTER TABLE Connection
ADD CONSTRAINT FOREIGN KEY (user_id) REFERENCES User(id);

ALTER TABLE User_Shop
ADD CONSTRAINT FOREIGN KEY (user_id) REFERENCES User(id);
ADD CONSTRAINT FOREIGN KEY (shop_id) REFERENCES Shop(id);