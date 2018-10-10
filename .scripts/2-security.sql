--
-- create database for storing users and passwords and fill it with test data
--

DROP DATABASE  IF EXISTS `logistika_users`;
CREATE DATABASE  IF NOT EXISTS `logistika_users`;
USE `logistika_users`;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(68) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `personalId` varchar(12) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
--

INSERT INTO `users` 
VALUES 
('obender','{bcrypt}$2a$10$0VswPVRBzNvQvxRQDOpzJeIFgcGZeNvURN0wd8z6S88NWjOdijiRm',1,'000001'),
('akozlevich','{bcrypt}$2a$10$lSxOGZAcNBq7CRiiJs/IKuY6B2YAPpOKT97ojwKUyMZJ.olzv2HkO',1,'000123'),
('abalaganov','{bcrypt}$2a$10$CgUHYU7TCNwV45hEz9H92OITn3R9zgS6LMXACJsUb0KSzM0SbL0pS',1,'000345');

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
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
('obender','ROLE_LOGIST'),
('akozlevich','ROLE_DRIVER'),
('abalaganov','ROLE_DRIVER');