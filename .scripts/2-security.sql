--
-- create database for storing users and first user (username = 'admin', password = 'admin', role = 'ROLE_LOGIST')
--

DROP DATABASE IF EXISTS `logistika_users`;
CREATE DATABASE `logistika_users`;
USE `logistika_users`;

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(68) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
--

INSERT INTO `users` 
VALUES 
('admin','{bcrypt}$2a$10$wMOMzGzr4X3bXFPITW8r8uIgWAlgQWWPA5Dd5sGImxkOYGOJpWNf6',1);

--
-- Table structure for table `authorities`
--

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `authorities`
--

INSERT INTO `authorities` 
VALUES 
('admin','ROLE_LOGIST');