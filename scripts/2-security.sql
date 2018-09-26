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
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `users`
--

INSERT INTO `users` 
VALUES 
('obender','{noop}go2rio',1),
('akozlevich','{noop}antilopa',1),
('abalaganov','{noop}suhar34users',1);

--- INSERT INTO `users` 
--- VALUES 
--- ('obender','{bcrypt}$2y$12$DJDkC.IMLcXvLBub2nMOQODCLMffZ18nLcq/1nj4JKdTDQcGBCNKK',1),
--- ('akozlevich','{bcrypt}$2y$12$ZJY2pB60uDNCew3WbLs3XubzUfIxXCNw3nGVYrAP7cWrgjLp2MIjK',1),
--- ('abalaganov','{bcrypt}$2y$12$PMKqkEZsS9rLcROjCHL/huIWqJBYKBnLx8HMJuc/jggQE7gW4T5TS',1);

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