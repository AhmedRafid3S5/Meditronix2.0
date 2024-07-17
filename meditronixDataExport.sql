CREATE DATABASE  IF NOT EXISTS `mylocaldb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mylocaldb`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mylocaldb
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `4d4u`
--

DROP TABLE IF EXISTS `4d4u`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `4d4u` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `4d4u`
--

LOCK TABLES `4d4u` WRITE;
/*!40000 ALTER TABLE `4d4u` DISABLE KEYS */;
INSERT INTO `4d4u` VALUES ('Prilosec','20mg',0,'1+1+0','2024-06-02','22:22:43');
/*!40000 ALTER TABLE `4d4u` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `4fir`
--

DROP TABLE IF EXISTS `4fir`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `4fir` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `4fir`
--

LOCK TABLES `4fir` WRITE;
/*!40000 ALTER TABLE `4fir` DISABLE KEYS */;
INSERT INTO `4fir` VALUES ('Azmasol','225g',0,'1+0+1','2024-06-02','22:16:48');
/*!40000 ALTER TABLE `4fir` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `5ee4`
--

DROP TABLE IF EXISTS `5ee4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `5ee4` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `5ee4`
--

LOCK TABLES `5ee4` WRITE;
/*!40000 ALTER TABLE `5ee4` DISABLE KEYS */;
INSERT INTO `5ee4` VALUES ('Azmasol','225ug',1,'1+0+1','2024-06-02','03:20:10');
/*!40000 ALTER TABLE `5ee4` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `5pku`
--

DROP TABLE IF EXISTS `5pku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `5pku` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `5pku`
--

LOCK TABLES `5pku` WRITE;
/*!40000 ALTER TABLE `5pku` DISABLE KEYS */;
INSERT INTO `5pku` VALUES ('Zyrtec','10mg',0,'1+1+0 For 1 week','2024-06-03','01:11:28'),('Paracetamol','500mg',0,'When needed','2024-06-03','01:11:28');
/*!40000 ALTER TABLE `5pku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `9w19`
--

DROP TABLE IF EXISTS `9w19`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `9w19` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `9w19`
--

LOCK TABLES `9w19` WRITE;
/*!40000 ALTER TABLE `9w19` DISABLE KEYS */;
INSERT INTO `9w19` VALUES ('Prednisolone','5mg',0,'1+1+1','2024-06-02','23:12:45');
/*!40000 ALTER TABLE `9w19` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events_info`
--

DROP TABLE IF EXISTS `events_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events_info` (
  `Building` int NOT NULL,
  `Room` varchar(45) NOT NULL,
  `event_date` date DEFAULT NULL,
  `Start_time` varchar(45) DEFAULT NULL,
  `End_time` varchar(45) DEFAULT NULL,
  `Event_info` varchar(700) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events_info`
--

LOCK TABLES `events_info` WRITE;
/*!40000 ALTER TABLE `events_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `events_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fyam`
--

DROP TABLE IF EXISTS `fyam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fyam` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fyam`
--

LOCK TABLES `fyam` WRITE;
/*!40000 ALTER TABLE `fyam` DISABLE KEYS */;
INSERT INTO `fyam` VALUES ('Minista','125ug',0,'1+0+1','2024-06-03','15:10:48');
/*!40000 ALTER TABLE `fyam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hhup`
--

DROP TABLE IF EXISTS `hhup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hhup` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hhup`
--

LOCK TABLES `hhup` WRITE;
/*!40000 ALTER TABLE `hhup` DISABLE KEYS */;
INSERT INTO `hhup` VALUES ('Ibuprofen','200mg',5,'1+0+1 for 30 days','2024-06-02','23:43:32');
/*!40000 ALTER TABLE `hhup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ic4u`
--

DROP TABLE IF EXISTS `ic4u`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ic4u` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ic4u`
--

LOCK TABLES `ic4u` WRITE;
/*!40000 ALTER TABLE `ic4u` DISABLE KEYS */;
INSERT INTO `ic4u` VALUES ('Azmasol','225ug',1,'1+0+1','2024-06-02','21:48:09');
/*!40000 ALTER TABLE `ic4u` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhir`
--

DROP TABLE IF EXISTS `jhir`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jhir` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhir`
--

LOCK TABLES `jhir` WRITE;
/*!40000 ALTER TABLE `jhir` DISABLE KEYS */;
INSERT INTO `jhir` VALUES ('Azmasol','225g',3,'1+1+1','2024-06-02','22:26:42');
/*!40000 ALTER TABLE `jhir` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `memos`
--

DROP TABLE IF EXISTS `memos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memos` (
  `No` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`No`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memos`
--

LOCK TABLES `memos` WRITE;
/*!40000 ALTER TABLE `memos` DISABLE KEYS */;
INSERT INTO `memos` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),(21),(22),(23),(24);
/*!40000 ALTER TABLE `memos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mlkz`
--

DROP TABLE IF EXISTS `mlkz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mlkz` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mlkz`
--

LOCK TABLES `mlkz` WRITE;
/*!40000 ALTER TABLE `mlkz` DISABLE KEYS */;
INSERT INTO `mlkz` VALUES ('Azmasol','225g',0,'1+1+0 for 1 month','2024-06-03','10:58:08'),('Ibuprofen','200mg',0,'1+0+1 for a month','2024-06-03','10:58:08');
/*!40000 ALTER TABLE `mlkz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ngll`
--

DROP TABLE IF EXISTS `ngll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ngll` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ngll`
--

LOCK TABLES `ngll` WRITE;
/*!40000 ALTER TABLE `ngll` DISABLE KEYS */;
INSERT INTO `ngll` VALUES ('Ibuprofen','500mg',1,'1+1+1','2024-06-02','22:11:10'),('Prednisolone','50mg',1,'1+1+0','2024-06-02','22:11:10');
/*!40000 ALTER TABLE `ngll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `omck`
--

DROP TABLE IF EXISTS `omck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `omck` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `omck`
--

LOCK TABLES `omck` WRITE;
/*!40000 ALTER TABLE `omck` DISABLE KEYS */;
INSERT INTO `omck` VALUES ('bakjcnak','50mg',1,'1+1+1'),('Dr Rajesh 35+','50mg',1,'1+0+1');
/*!40000 ALTER TABLE `omck` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uniqueid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (9,'5EE4','Rafid',21,'Male','2024-06-02','03:20:10'),(10,'IC4U','Ahmed Rafid',21,'Male','2024-06-02','21:48:09'),(11,'NGLL','Rumman Adib',22,'Male','2024-06-02','22:11:10'),(12,'4FIR','Ahmed Rafid',21,'Male','2024-06-02','22:16:48'),(13,'4D4U','Ahmed Rafid',21,'Male','2024-06-02','22:22:43'),(14,'JHIR','Ahmed Rafid',21,'Male','2024-06-02','22:26:42'),(15,'9W19','Ahmed Rafid',21,'Male','2024-06-02','23:12:45'),(16,'HHUP','Rumman Adib',21,'Male','2024-06-02','23:43:32'),(17,'5PKU','Rumman Adib',21,'Male','2024-06-03','01:11:28'),(18,'MLKZ','Ahmed Rafid',21,'Male','2024-06-03','10:58:08'),(19,'WXHC','Ahmed Rafid',21,'Male','2024-06-03','14:16:03'),(20,'UA9K','Ahmed rafid',21,'Male','2024-06-03','14:26:14'),(21,'FYAM','ahmed rafid',22,'Male','2024-06-03','15:10:48');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `poll_table`
--

DROP TABLE IF EXISTS `poll_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `poll_table` (
  `poll_id` int NOT NULL AUTO_INCREMENT,
  `poll_url` varchar(500) DEFAULT NULL,
  `poll_title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`poll_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `poll_table`
--

LOCK TABLES `poll_table` WRITE;
/*!40000 ALTER TABLE `poll_table` DISABLE KEYS */;
INSERT INTO `poll_table` VALUES (1,'https://forms.gle/DVhnjMWWKMhzfTXp9','Friday routine change vote');
/*!40000 ALTER TABLE `poll_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shop_inventory`
--

DROP TABLE IF EXISTS `shop_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop_inventory` (
  `serial_id` varchar(100) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Dose` varchar(50) DEFAULT NULL,
  `Selling_price` float DEFAULT NULL,
  `Expiry` date DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Available_Quantity` float DEFAULT NULL,
  `unit_cost` float DEFAULT NULL,
  PRIMARY KEY (`serial_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shop_inventory`
--

LOCK TABLES `shop_inventory` WRITE;
/*!40000 ALTER TABLE `shop_inventory` DISABLE KEYS */;
INSERT INTO `shop_inventory` VALUES ('2024-04-08 01:19:12','Lopirel','40mg',90,'2028-01-01','Prescription',20,85),('2024-04-08 01:25:27','Bexitrol','125ug',1000,'2028-01-01','Prescription',5,850),('2024-04-08 01:26:02','Minista','125ug',100,'2028-01-01','Prescription',4,85),('2024-04-08 14:47:26','Minista','50mg',65.25,'2100-12-31','Prescription',20,50),('2024-04-08 14:48:54','Favilax','100ug',65.25,'2025-12-31','Prescription',10.5,50.5),('2024-04-08 14:50:13','Clopid Plus','100mg',65.25,'2025-12-31','Prescription',25,50.5),('2024-04-08 14:51:05','Dr_Razes+ Mosquito Cream','250ml',150,'2027-12-31','Generic',0,100),('2024-04-22 23:46:52','NewTestMEd','-',14,'2024-04-27','Generic',46,0),('2024-04-22 23:54:48','Random+','50mg',35,'2025-04-26','Generic',1,0),('2024-05-09 18:36:45','Aderall Extra','55mg',50,'2025-12-31','Generic',41,0),('2024-05-09 18:37:22','Aderall Extra','55mg',50,'2025-12-08','Generic',50,0),('2024-06-02 14:57:54','Cefotil','500mg',60,'2026-11-01','Prescription',10,50),('2024-06-02 15:02:28','Panadol Advance','500mg',30,'2025-06-01','Generic',12,25),('2024-06-02 15:02:29','Advil','200mg',45,'2025-09-15','Prescription',50,40),('2024-06-02 15:02:30','Tylenol','500mg',35,'2023-07-20','Prescription',20,30),('2024-06-02 15:02:31','Aspirin','300mg',25,'2025-10-10','Generic',59,20),('2024-06-02 15:02:32','Nurofen','200mg',50,'2025-08-05','Prescription',45,42),('2024-06-02 15:02:33','Aleve','220mg',40,'2025-06-30','Prescription',30,35),('2024-06-02 15:02:34','Voltaren','50mg',60,'2025-11-12','Prescription',25,55),('2024-06-02 15:02:35','Motrin','400mg',45,'2025-04-25','Prescription',40,37),('2024-06-02 15:02:36','Ibuprofen','200mg',25,'2025-08-20','Generic',69,22),('2024-06-02 15:02:37','Paracetamol','500mg',20,'2025-03-15','Generic',80,15),('2024-06-02 15:02:38','Zyrtec','10mg',55,'2025-05-01','Prescription',31,50),('2024-06-02 15:02:39','Claritin','10mg',50,'2025-09-25','Prescription',45,45),('2024-06-02 15:02:40','Benadryl','25mg',30,'2025-07-10','Prescription',50,27),('2024-06-02 15:02:41','Sudafed','30mg',35,'2025-08-01','Prescription',25,32),('2024-06-02 15:02:42','Mucinex','600mg',65,'2025-10-20','Prescription',20,60),('2024-06-02 15:02:43','NyQuil','15mg',70,'2023-12-15','Prescription',15,65),('2024-06-02 15:02:44','DayQuil','15mg',55,'2025-11-10','Prescription',20,50),('2024-06-02 15:02:45','Pepto-Bismol','262mg',40,'2025-10-05','Generic',0,35),('2024-06-02 15:02:46','Imodium','2mg',45,'2025-09-15','Prescription',25,40),('2024-06-02 15:02:47','Tums','500mg',25,'2025-08-30','Generic',0,22),('2024-06-02 15:02:48','Cetirizine','10mg',15,'2025-06-02','Generic',99,12),('2024-06-02 15:02:49','Loratadine','10mg',18,'2025-06-02','Generic',4,15),('2024-06-02 15:02:50','Dextromethorphan','30mg',20,'2025-06-02','Generic',3,17),('2024-06-02 15:02:51','Guaifenesin','600mg',25,'2025-06-02','Generic',1,20),('2024-06-02 15:02:52','Loperamide','2mg',22,'2025-06-02','Generic',75,19),('2024-06-02 15:02:53','Simethicone','80mg',18,'2025-06-02','Generic',84,15),('2024-06-02 15:02:54','Phenylephrine','10mg',20,'2025-06-02','Generic',80,17),('2024-06-02 15:02:55','Diphenhydramine','25mg',15,'2025-06-02','Generic',88,12),('2024-06-02 15:02:56','Hydrocortisone Cream','1ml',12,'2025-06-02','Generic',100,10),('2024-06-02 15:02:57','Saline Nasal Spray','9ml',10,'2025-06-02','Generic',0,8),('2024-06-02 15:10:04','Paracitamo','500mg',60,'2026-06-01','Generic',19,55),('2024-06-03 15:08:37','Azmasol','125ug',50,'2025-06-30','Prescription',10,45);
/*!40000 ALTER TABLE `shop_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_parameters`
--

DROP TABLE IF EXISTS `stock_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_parameters` (
  `lowStockValue` int NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`lowStockValue`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_parameters`
--

LOCK TABLES `stock_parameters` WRITE;
/*!40000 ALTER TABLE `stock_parameters` DISABLE KEYS */;
INSERT INTO `stock_parameters` VALUES (16,0);
/*!40000 ALTER TABLE `stock_parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_requests`
--

DROP TABLE IF EXISTS `student_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_requests` (
  `ID` int NOT NULL,
  `Request` varchar(500) NOT NULL,
  `request_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_requests`
--

LOCK TABLES `student_requests` WRITE;
/*!40000 ALTER TABLE `student_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `student_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ua9k`
--

DROP TABLE IF EXISTS `ua9k`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ua9k` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ua9k`
--

LOCK TABLES `ua9k` WRITE;
/*!40000 ALTER TABLE `ua9k` DISABLE KEYS */;
INSERT INTO `ua9k` VALUES ('Favilax','100ug',1,'1+0+1','2024-06-03','14:26:14'),('Bexitrol','499ug',3,'1+0+0','2024-06-03','14:26:14');
/*!40000 ALTER TABLE `ua9k` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('admin1','1234','customer'),('admin2','1234','doctor'),('arafid360','stayfrosty','pharmacist'),('arafid365','fbd2d569888aea46c4b23f984aad4bd3c04750b49e509503247b1a6f0a230fe5','pharmacist'),('doctor1','089542505d659cecbb988bb5ccff5bccf85be2dfa8c221359079aee2531298bb','doctor'),('fariya','imsick','customer'),('patient1','089542505d659cecbb988bb5ccff5bccf85be2dfa8c221359079aee2531298bb','customer'),('patient2','089542505d659cecbb988bb5ccff5bccf85be2dfa8c221359079aee2531298bb','customer'),('rafid','360','customer'),('rumman','ainnoway','doctor');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wxhc`
--

DROP TABLE IF EXISTS `wxhc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wxhc` (
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `frequency` varchar(50) DEFAULT NULL,
  `generated_date` date DEFAULT NULL,
  `generated_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wxhc`
--

LOCK TABLES `wxhc` WRITE;
/*!40000 ALTER TABLE `wxhc` DISABLE KEYS */;
INSERT INTO `wxhc` VALUES ('Azmasol','225g',3,'1+0+1','2024-06-03','14:16:03');
/*!40000 ALTER TABLE `wxhc` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-17 21:29:57
