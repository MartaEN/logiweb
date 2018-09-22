CREATE DATABASE  IF NOT EXISTS `logistika`;
USE `logistika`;

-- MySQL dump 10.13  Distrib 5.7.21, for Win64 (x86_64)
--
-- Host: localhost    Database: logistika
-- ------------------------------------------------------
-- Server version	5.7.21-log

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
CREATE TABLE `cities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l61tawv0e2a93es77jkyvi7qa` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Table structure for table `drivers`
--

DROP TABLE IF EXISTS `drivers`;
CREATE TABLE `drivers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bookedUntil` datetime(6) NOT NULL,
  `firstName` varchar(65) NOT NULL,
  `lastName` varchar(65) NOT NULL,
  `personalId` varchar(12) NOT NULL,
  `location` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gq374lh46a7wq07rhyiuwt27m` (`personalId`),
  KEY `FK5yhcy90whkuux8j8h8r07gsfc` (`location`),
  CONSTRAINT `FK5yhcy90whkuux8j8h8r07gsfc` FOREIGN KEY (`location`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime(6) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `fromCity` bigint(20) NOT NULL,
  `toCity` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcbv7g0mx53fqfb0e318erdcfb` (`fromCity`),
  KEY `FKqtovmi7c9fcr4rahgoq7guaml` (`toCity`),
  CONSTRAINT `FKcbv7g0mx53fqfb0e318erdcfb` FOREIGN KEY (`fromCity`) REFERENCES `cities` (`id`),
  CONSTRAINT `FKqtovmi7c9fcr4rahgoq7guaml` FOREIGN KEY (`toCity`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


--
-- Table structure for table `roads`
--

DROP TABLE IF EXISTS `roads`;
CREATE TABLE `roads` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distance` int(11) NOT NULL,
  `fromCity` bigint(20) NOT NULL,
  `toCity` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhh9vlehh5r2ccb7bkhmj7uvgd` (`fromCity`),
  KEY `FKgn48sywc7s7slaxs7m7xnyel9` (`toCity`),
  CONSTRAINT `FKgn48sywc7s7slaxs7m7xnyel9` FOREIGN KEY (`toCity`) REFERENCES `cities` (`id`),
  CONSTRAINT `FKhh9vlehh5r2ccb7bkhmj7uvgd` FOREIGN KEY (`fromCity`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;


--
-- Table structure for table `shifts`
--

DROP TABLE IF EXISTS `shifts`;
CREATE TABLE `shifts` (
  `trip` bigint(20) NOT NULL,
  `driver` bigint(20) NOT NULL,
  KEY `FK1nelyr4dj34suwxmf6k58k51b` (`driver`),
  KEY `FKby7d377ajtxfcakb3y5scdua0` (`trip`),
  CONSTRAINT `FK1nelyr4dj34suwxmf6k58k51b` FOREIGN KEY (`driver`) REFERENCES `drivers` (`id`),
  CONSTRAINT `FKby7d377ajtxfcakb3y5scdua0` FOREIGN KEY (`trip`) REFERENCES `tickets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `stopovers`
--

DROP TABLE IF EXISTS `stopovers`;
CREATE TABLE `stopovers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `duration` bigint(20) DEFAULT NULL,
  `sequenceNo` int(11) NOT NULL,
  `weight` int(11) DEFAULT NULL,
  `city` bigint(20) NOT NULL,
  `trip` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2nhhrxsjx1iiywu72g6rmdydu` (`city`),
  KEY `FKpfp4fpgk15f22k6s4greeolr8` (`trip`),
  CONSTRAINT `FK2nhhrxsjx1iiywu72g6rmdydu` FOREIGN KEY (`city`) REFERENCES `cities` (`id`),
  CONSTRAINT `FKpfp4fpgk15f22k6s4greeolr8` FOREIGN KEY (`trip`) REFERENCES `tickets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
CREATE TABLE `tickets` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `arrival` datetime(6) DEFAULT NULL,
  `departure` datetime(6) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `truck` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3mo9x179xoplvp0tq0y920d8r` (`truck`),
  CONSTRAINT `FK3mo9x179xoplvp0tq0y920d8r` FOREIGN KEY (`truck`) REFERENCES `trucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `time_tracker`
--

DROP TABLE IF EXISTS `time_tracker`;
CREATE TABLE `time_tracker` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `finish` datetime(6) DEFAULT NULL,
  `minutes` bigint(20) DEFAULT NULL,
  `start` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `driver` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5qsbxtn27vian263fx7awoayw` (`driver`),
  CONSTRAINT `FK5qsbxtn27vian263fx7awoayw` FOREIGN KEY (`driver`) REFERENCES `drivers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
  `type` varchar(6) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `stopover` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfyxndk58yiq2vpn0yd4m09kbt` (`order_id`),
  KEY `FK6cip975i2cm6jheh4bdb391wn` (`stopover`),
  CONSTRAINT `FK6cip975i2cm6jheh4bdb391wn` FOREIGN KEY (`stopover`) REFERENCES `stopovers` (`id`),
  CONSTRAINT `FKfyxndk58yiq2vpn0yd4m09kbt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `trucks`
--

DROP TABLE IF EXISTS `trucks`;
CREATE TABLE `trucks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bookedUntil` datetime(6) NOT NULL,
  `capacity` int(11) NOT NULL,
  `isParked` bit(1) NOT NULL,
  `isServiceable` bit(1) NOT NULL,
  `regNumber` varchar(7) NOT NULL,
  `shiftSize` int(11) NOT NULL,
  `location` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k9gnwpt2w3e1m25ft4nvm6vsy` (`regNumber`),
  KEY `FKnra9j9k3wvnmtm87u03q2lyi4` (`location`),
  CONSTRAINT `FKnra9j9k3wvnmtm87u03q2lyi4` FOREIGN KEY (`location`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- Dump completed on 2018-09-22 16:01:49
