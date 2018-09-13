CREATE DATABASE  IF NOT EXISTS `logistika`;
USE `logistika`;

-- MySQL dump 10.13  Distrib 5.7.21, for Win64 (x86_64)
--
-- Host: localhost    Database: logistika
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l61tawv0e2a93es77jkyvi7qa` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (7,'Вологда'),(6,'Москва'),(2,'Санкт-Петербург'),(14,'Старая Ладога'),(3,'Тверь'),(9,'Тихвин'),(8,'Череповец'),(5,'Ярославль');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roads`
--

DROP TABLE IF EXISTS `roads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roads`
--

LOCK TABLES `roads` WRITE;
/*!40000 ALTER TABLE `roads` DISABLE KEYS */;
INSERT INTO `roads` VALUES (5,533,3,2),(6,533,2,3),(7,328,5,3),(8,328,3,5),(9,176,6,3),(10,176,3,6),(11,272,6,5),(12,272,5,6),(15,196,7,5),(16,196,5,7),(17,319,9,8),(18,319,8,9),(21,134,14,2),(22,134,2,14),(25,105,9,14),(26,105,14,9),(31,136,7,8),(32,136,8,7);
/*!40000 ALTER TABLE `roads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trucks`
--

DROP TABLE IF EXISTS `trucks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trucks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacity` int(11) NOT NULL,
  `isParked` tinyint(1) NOT NULL DEFAULT '1',
  `isServiceable` bit(1) NOT NULL,
  `regNumber` varchar(7) NOT NULL,
  `shiftSize` int(11) NOT NULL,
  `location` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k9gnwpt2w3e1m25ft4nvm6vsy` (`regNumber`),
  KEY `FKnra9j9k3wvnmtm87u03q2lyi4` (`location`),
  CONSTRAINT `FKnra9j9k3wvnmtm87u03q2lyi4` FOREIGN KEY (`location`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trucks`
--

LOCK TABLES `trucks` WRITE;
/*!40000 ALTER TABLE `trucks` DISABLE KEYS */;
INSERT INTO `trucks` VALUES (1,1500,0,'','AA00000',1,3),(2,3500,0,'\0','AB12345',1,7),(3,6500,1,'','AC33333',2,8),(4,9000,1,'','BA98765',2,5);
/*!40000 ALTER TABLE `trucks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-13  9:22:14
