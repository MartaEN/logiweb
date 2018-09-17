USE `logistika`;

LOCK TABLES `cities` WRITE;
INSERT INTO `cities` VALUES (6,'Вологда'),(4,'Москва'),(1,'Санкт-Петербург'),(2,'Старая Ладога'),(3,'Тверь'),(8,'Тихвин'),(7,'Череповец'),(5,'Ярославль');
UNLOCK TABLES;


LOCK TABLES `drivers` WRITE;
INSERT INTO `drivers` VALUES (1,'Адам','Козлевич','000123','+7(123)456-78-90'),(2,'Михаель','Шумахер','000777','+7(999)777-77-77'),(3,'Фрэнк','Мартин','000999','+(999)999-99-99');
UNLOCK TABLES;

LOCK TABLES `orders` WRITE;
INSERT INTO `orders` VALUES (1,'2018-09-17 03:19:23.958000','First order in the system','NEW',3500,1,7),(2,'2018-09-17 03:19:24.316000','Second order in the system','NEW',1000,2,6);
UNLOCK TABLES;

LOCK TABLES `roads` WRITE;
INSERT INTO `roads` VALUES (1,134,2,1),(2,134,1,2),(3,533,3,1),(4,533,1,3),(5,176,4,3),(6,176,3,4),(7,328,5,3),(8,328,3,5),(9,272,5,4),(10,272,4,5),(11,196,6,5),(12,196,5,6),(13,136,7,6),(14,136,6,7),(15,319,8,7),(16,319,7,8),(17,105,8,2),(18,105,2,8);
UNLOCK TABLES;

LOCK TABLES `trucks` WRITE;
INSERT INTO `trucks` VALUES (1,'2018-09-17 03:17:54.103000',5000,1,1,'AA00000',1,1),(2,'2018-09-17 03:18:07.669000',20000,1,1,'AB12345',2,7),(3,'2018-09-17 03:18:22.727000',15000,1,0,'AC33333',2,5);
UNLOCK TABLES;
