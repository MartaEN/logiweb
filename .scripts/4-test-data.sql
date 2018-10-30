-- Create test users

USE `logistika_users`;

INSERT INTO `users` VALUES 
	('obender','{bcrypt}$2a$10$0VswPVRBzNvQvxRQDOpzJeIFgcGZeNvURN0wd8z6S88NWjOdijiRm',1),
	('akozlevich','{bcrypt}$2a$10$lSxOGZAcNBq7CRiiJs/IKuY6B2YAPpOKT97ojwKUyMZJ.olzv2HkO',1),
	('abalaganov','{bcrypt}$2a$10$CgUHYU7TCNwV45hEz9H92OITn3R9zgS6LMXACJsUb0KSzM0SbL0pS',1),
	('mpanikovski','{bcrypt}$2a$10$iv5ShgUqjhK7exBn0zkhIuDXnombek7tIbsvbsnEfXrtFKdSPgqzS',1);

INSERT INTO `authorities` VALUES 
	('obender','ROLE_LOGIST'),
	('akozlevich','ROLE_DRIVER'),
	('abalaganov','ROLE_DRIVER'),
	('mpanikovski','ROLE_DRIVER');




-- Create test business data

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
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (6,'Вологда'),
							(4,'Москва'),
							(1,'Санкт-Петербург'),
                            (2,'Старая Ладога'),
                            (3,'Тверь'),
                            (8,'Тихвин'),
                            (7,'Череповец'),
                            (5,'Ярославль');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roads`
--

LOCK TABLES `roads` WRITE;
/*!40000 ALTER TABLE `roads` DISABLE KEYS */;
INSERT INTO `roads` VALUES 	(1,134,2,1),(2,134,1,2),
							(3,533,3,1),(4,533,1,3),
                            (5,176,4,3),(6,176,3,4),
                            (7,328,5,3),(8,328,3,5),
                            (9,272,5,4),(10,272,4,5),
                            (11,196,6,5),(12,196,5,6),
                            (13,136,7,6),(14,136,6,7),
                            (15,319,8,7),(16,319,7,8),
                            (17,105,8,2),(18,105,2,8);
/*!40000 ALTER TABLE `roads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `trucks`
--

LOCK TABLES `trucks` WRITE;
/*!40000 ALTER TABLE `trucks` DISABLE KEYS */;
INSERT INTO `trucks` VALUES (1,'2018-09-28 09:20:30.582000',5000,1,1,'AA00000',1,1),
							(2,'2018-09-17 03:18:07.669000',20000,1,0,'AB12345',2,7),
							(3,'2018-09-17 03:18:22.727000',5000,0,1,'AC33333',1,1),
							(5,'2099-12-31 23:59:00.000000',20000,0,1,'ZZ99999',2,1);
/*!40000 ALTER TABLE `trucks` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `drivers`
--

LOCK TABLES `drivers` WRITE;
/*!40000 ALTER TABLE `drivers` DISABLE KEYS */;
INSERT INTO `drivers` VALUES (1,'2018-09-01 00:00:00.000000','Адам','Козлевич','000123','akozlevich',1,'OFFLINE',0),
							(2,'2018-09-26 20:00:00.000000','Александр','Балаганов','000345','abalaganov',1,'OFFLINE',0),
                            (3,'2018-09-01 00:00:00.000000','Михаил','Паниковский','000013','mpanikovski',5,'DRIVING',0);
/*!40000 ALTER TABLE `drivers` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2018-09-17 13:19:24.000000','Слон','DELIVERED',1200,1,7),
							(2,'2018-09-17 13:29:34.000000','Бегемот','DELIVERED',1000,2,6),
                            (3,'2018-10-17 03:19:24.000000','Пиво','NEW',4000,5,1),
                            (4,'2018-10-17 03:19:24.000000','Лом цветных металлов','ASSIGNED',650,7,1),
                            (5,'2018-10-18 15:08:13.000000','Шампунь финский','ASSIGNED',1000,1,7),
                            (6,'2018-10-18 10:44:05.000000','Швеллер гнутый равнополочный','NEW',3500,7,1),
                            (7,'2018-10-24 10:45:37.000000','Буссоли','NEW',1300,6,8),
                            (8,'2018-10-24 11:02:50.000000','Масло сливочное 82.5%','NEW',3000,6,4),
                            (9,'2018-10-25 11:02:50.000000','Турбинные лопатки','NEW',4500,1,4),
                            (10,'2018-10-25 14:02:50.000000','Картофель','NEW',2700,6,7),
                            (11,'2018-10-25 14:02:50.000000','Капуста','NEW',1700,6,1),
                            (12,'2018-10-25 15:02:50.000000','Щебень строительный','NEW',2400,1,6),
                            (13,'2018-10-26 15:45:37.000000','Масло сливочное вологодское','NEW',2200,6,8),
                            (14,'2018-10-26 16:02:50.000000','Апельсины бочками','NEW',3200,6,1),
                            (15,'2018-10-26 16:08:13.000000','Бананы','NEW',1500,1,7),
                            (16,'2018-10-27 16:14:05.000000','Сталепрокат','NEW',1300,7,1),
                            (17,'2018-10-27 16:22:50.000000','Огурцы малосольные','NEW',800,6,1),
                            (18,'2018-10-27 16:24:50.000000','Вертолет','NEW',2500,1,4),
                            (19,'2018-10-27 16:28:50.000000','Лесоматериалы','NEW',5000,1,4),
                            (20,'2018-10-28 03:19:24.000000','Сиг г/к','NEW',800,2,6);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (12,'2018-09-26 20:00:00.000000',17,4,'2018-09-25 09:00:00.000000','CLOSED',1),
							(13,'2018-10-29 20:00:00.000000',0,0,'2018-09-30 09:00:00.000000','RUNNING',3),
							(14,NULL,16,0,'2018-09-29 09:00:00.000000','CREATED',5);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `stopovers`
--

LOCK TABLES `stopovers` WRITE;
/*!40000 ALTER TABLE `stopovers` DISABLE KEYS */;
INSERT INTO `stopovers` VALUES (1,7200000000000,0,1200,1,12),(2,46800000000000,4,0,1,12),
			(3,32400000000000,2,1000,7,12),(4,14400000000000,1,2200,2,12),(5,14400000000000,3,0,6,12),
			(6,46800000000000,0,0,5,13),(7,32400000000000,1,0,1,13),
            (8,NULL,0,1000,1,14),(9,NULL,2,0,1,14),(10,NULL,1,650,7,14);
/*!40000 ALTER TABLE `stopovers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES ('LOAD',1,1,1),('UNLOAD',2,1,3),('LOAD',3,2,4),('UNLOAD',4,2,5),('LOAD',5,5,8),('UNLOAD',6,5,10),('LOAD',7,4,10),('UNLOAD',8,4,9);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `shifts`
--

LOCK TABLES `shifts` WRITE;
/*!40000 ALTER TABLE `shifts` DISABLE KEYS */;
INSERT INTO `shifts` VALUES (12,2), (13, 3);
/*!40000 ALTER TABLE `shifts` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES `time_tracker` WRITE;
/*!40000 ALTER TABLE `time_tracker` DISABLE KEYS */;
INSERT INTO `time_tracker` (id, start, minutes, status, driver) VALUES (1, '2018-10-29 20:00:00.000000', 0, 'DRIVING', 3);
/*!40000 ALTER TABLE `time_tracker` ENABLE KEYS */;
UNLOCK TABLES;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-28 11:04:35
