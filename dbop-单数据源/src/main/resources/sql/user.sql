CREATE TABLE  IF NOT EXISTS user (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(32) NULL DEFAULT '',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;