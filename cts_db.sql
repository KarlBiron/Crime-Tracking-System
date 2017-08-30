
DROP database IF EXISTS cts_db ;
CREATE DATABASE `cts_db` ;
USE cts_db;

DROP TABLE  IF EXISTS investigators_tbl;

CREATE TABLE investigators_tbl (
id smallint auto_increment primary key not null,
username varchar(1024),
password varchar(1024),
fullname varchar(1024),
email varchar(1024)
);

INSERT INTO `cts_db`.`investigators_tbl`
(
`username`,
`password`)
VALUES
( 'user1', '1'),
( 'user2', '2');

DROP table IF EXISTS crimes_tbl ;

CREATE TABLE crimes_tbl (
reference int(5) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT primary key,
date_time timestamp,
location varchar(1024),
victims varchar(1024),
suspects  varchar(1024),
evidences  varchar(1024),
created timestamp default current_timestamp,
fulltext(location, victims, suspects, evidences)


);





INSERT INTO `cts_db`.`crimes_tbl`
(
`victims`,
`location`,

`suspects`
)
VALUES
( 'victims_1', 'location_1', 'suspects_1 nothing to do'),
( 'victims_2', 'location_2', 'suspects_2'),
( 'victims_3', 'location_3', 'suspects_3'),
( 'victims_4', 'location_4', 'suspects_4'),
( 'victims_5', 'location_5', 'suspects_5'),
( 'victims_5', 'location_5', 'suspects_5');

INSERT INTO `cts_db`.`crimes_tbl`
(
`victims`,
`location`,
`date_time`,
`suspects`
)
VALUES
( 'victims_6', 'location_6',  '2013-08-05 18:19:03', 'suspects_6'),
( 'victims_7', 'location_7',  '2013-08-05 18:19:03',  'suspects_7'),
( 'victims_8', 'location_8',  '2013-08-05 18:19:03',  'suspects_8'),
( 'victims_9', 'location_9',  '2013-08-05 18:19:03', 'suspects_9'),
( 'victims_10','location_10', '2017-03-09 00:20:00', 'suspects_10'),
( 'victims_11','location_10', '2017-03-09 00:20:00', 'suspects_11');
select * from crimes_tbl;

DROP USER IF EXISTS investigator1;
FLUSH PRIVILEGES;
CREATE USER 'investigator1'@'localhost' IDENTIFIED BY '1';
GRANT ALL PRIVILEGES ON * . * TO 'investigator1'@'localhost';
FLUSH PRIVILEGES;

 