CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';

CREATE DATABASE  IF NOT EXISTS `logistika`;
GRANT ALL PRIVILEGES ON `logistika` . * TO 'test'@'localhost';

CREATE DATABASE IF NOT EXISTS `logistika_users`;
GRANT ALL PRIVILEGES ON `logistika_users` . * TO 'test'@'localhost';

FLUSH PRIVILEGES;