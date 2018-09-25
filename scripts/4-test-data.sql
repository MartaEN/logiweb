USE `logistika`;

LOCK TABLES `cities` WRITE;
INSERT INTO `cities` VALUES (6,'Вологда'),(4,'Москва'),(1,'Санкт-Петербург'),(2,'Старая Ладога'),(3,'Тверь'),(8,'Тихвин'),(7,'Череповец'),(5,'Ярославль');
UNLOCK TABLES;

LOCK TABLES `drivers` WRITE;
INSERT INTO `drivers` (`bookedUntil`, `firstName`, `lastName`, `personalId`, `location`) VALUES ('2018-09-01 00:00', 'Адам', 'Козлевич', '000123', '1'), ('2018-09-01 00:00', 'Фрэнк', 'Мартин', '000777', '1'), ('2018-09-01 00:00', 'Михаэль', 'Шумахер', '000999', '1');
UNLOCK TABLES;

LOCK TABLES `orders` WRITE;
INSERT INTO `orders` VALUES (1,'2018-09-17 03:19:23.958000','Слон','NEW',1200,1,7),(2,'2018-09-17 03:19:24.316000','Бегемот','NEW',1000,2,6),(3,'2018-09-17 03:19:24.316000','Гусь','NEW',50,2,6),(4,'2018-09-17 03:19:24.316000','Поросенок','NEW',100,7,1);
UNLOCK TABLES;

LOCK TABLES `roads` WRITE;
INSERT INTO `roads` VALUES (1,134,2,1),(2,134,1,2),(3,533,3,1),(4,533,1,3),(5,176,4,3),(6,176,3,4),(7,328,5,3),(8,328,3,5),(9,272,5,4),(10,272,4,5),(11,196,6,5),(12,196,5,6),(13,136,7,6),(14,136,6,7),(15,319,8,7),(16,319,7,8),(17,105,8,2),(18,105,2,8);
UNLOCK TABLES;

LOCK TABLES `stopovers` WRITE;
INSERT INTO `stopovers` VALUES (1,NULL,0,1200,1,1),(2,NULL,2,0,1,1),(3,NULL,1,100,7,1);
UNLOCK TABLES;

LOCK TABLES `tickets` WRITE;
INSERT INTO `tickets` VALUES (1,NULL,13,'2018-09-25 21:00:00.000000','CREATED',1);
UNLOCK TABLES;

LOCK TABLES `transactions` WRITE;
INSERT INTO `transactions` VALUES ('LOAD',1,1,1),('UNLOAD',2,1,3),('LOAD',3,4,3),('UNLOAD',4,4,2);
UNLOCK TABLES;

LOCK TABLES `trucks` WRITE;
INSERT INTO `trucks` VALUES (1,'2018-09-17 03:17:54.103000',5000,1,1,'AA00000',1,1),(2,'2018-09-17 03:18:07.669000',20000,1,1,'AB12345',2,7),(3,'2018-09-17 03:18:22.727000',15000,1,0,'AC33333',2,5);
UNLOCK TABLES;
