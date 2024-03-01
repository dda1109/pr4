/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
SET NAMES utf8;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `address`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `user_id`     int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKda8tuywtf0gb6sedwk7la1pgi` (`user_id`),
    CONSTRAINT `FKda8tuywtf0gb6sedwk7la1pgi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address`
    DISABLE KEYS */;
INSERT INTO `address`
VALUES (1, 'Số 1, Võ Văn Ngân, Thủ Đức, TP.HCM', 4),
       (2, '123 Nguyễn Kiệm, Tân Bình, Tp.HCM', 4),
       (3, 'Highland Coffe, phường 26, Bình Thạnh, TP.HCM', 4),
       (4, '296 Chu Văn An, phường Bến Nghé, Gò Vấp, Tp.HCM', 5),
       (5, 'Nội Bài, Hà Nội', 3),
       (6, 'Vũng Tàu', 10),
       (7, 'Quận 1, TP.HCM', 8),
       (8, 'Nội Bài, Hà Nội', 17);
/*!40000 ALTER TABLE `address`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `bill`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `date`    date    DEFAULT NULL,
    `status`  int(11) DEFAULT NULL,
    `total`   double  NOT NULL,
    `user_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKqhq5aolak9ku5x5mx11cpjad9` (`user_id`),
    CONSTRAINT `FKqhq5aolak9ku5x5mx11cpjad9` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill`
    DISABLE KEYS */;
INSERT INTO `bill`
VALUES (1, '2022-05-12', 1, 3427, 4),
       (2, '2022-05-12', 1, 2694, 4),
       (3, '2022-05-12', 1, 5793, 4),
       (4, '2022-05-12', 1, 7965, 4),
       (5, '2022-05-12', 1, 10748, 4),
       (6, '2022-05-12', 1, 9710, 5),
       (7, '2022-05-25', 1, 10229, 4),
       (8, '2022-05-21', 1, 545, 3),
       (9, '2022-05-25', 1, 3400, 4),
       (10, '2022-05-25', 1, 5950, 4),
       (11, '2022-05-25', 1, 5460, 4);
/*!40000 ALTER TABLE `bill`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_detail`
--

DROP TABLE IF EXISTS `bill_detail`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `bill_detail`
(
    `bill_id`    int(11) NOT NULL,
    `product_id` int(11) NOT NULL,
    `quantity`   int(11) NOT NULL,
    PRIMARY KEY (`bill_id`, `product_id`),
    KEY `FKe7fmo7042u349ftue4g4oeiuy` (`product_id`),
    CONSTRAINT `FKe7fmo7042u349ftue4g4oeiuy` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `FKeolgwyayei3o80bb7rj7t207q` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_detail`
--

LOCK TABLES `bill_detail` WRITE;
/*!40000 ALTER TABLE `bill_detail`
    DISABLE KEYS */;
INSERT INTO `bill_detail`
VALUES (1, 6, 2),
       (1, 7, 28),
       (1, 11, 43),
       (1, 12, 2),
       (1, 13, 43),
       (1, 14, 42),
       (1, 15, 39),
       (2, 6, 7),
       (3, 6, 33),
       (3, 11, 44),
       (4, 5, 28),
       (4, 6, 34),
       (4, 13, 44),
       (5, 8, 3),
       (5, 10, 12),
       (5, 14, 45),
       (5, 15, 35),
       (6, 7, 27),
       (6, 9, 3),
       (6, 11, 9),
       (7, 5, 3),
       (7, 7, 29),
       (7, 10, 9),
       (8, 8, 7),
       (9, 7, 4),
       (10, 12, 7),
       (11, 9, 7);
/*!40000 ALTER TABLE `bill_detail`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `card`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `bank`    varchar(255) DEFAULT NULL,
    `number`  varchar(255) DEFAULT NULL,
    `user_id` int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKl4gbym62l738id056y12rt6q6` (`user_id`),
    CONSTRAINT `FKl4gbym62l738id056y12rt6q6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card`
    DISABLE KEYS */;
INSERT INTO `card`
VALUES (1, 'VCB', '34059345', 4),
       (2, 'TP', '8345089543', 4),
       (3, 'SAC', '34583405', 4),
       (4, 'TP', '2394307420934', 4),
       (5, 'VCB', '810398120381', 3),
       (6, 'VCB', '06496465487', 8),
       (7, 'SAC', '7987646532132', 13),
       (8, 'SAC', '689712132164', 15);
/*!40000 ALTER TABLE `card`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `category`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category`
    DISABLE KEYS */;
INSERT INTO `category`
VALUES (1, 'Đồng Hồ'),
       (2, 'Ví Cầm Tay'),
       (3, 'Ví Dài'),
       (4, 'Balo'),
       (5, 'Thắt Lưng'),
       (6, 'Giày'),
       (7, 'Dép'),
       (8, 'Vớ'),
       (9, 'Nón');
/*!40000 ALTER TABLE `category`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_info`
--

DROP TABLE IF EXISTS `customer_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `customer_info`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `bithday`  date         DEFAULT NULL,
    `email`    varchar(255) DEFAULT NULL,
    `fullname` varchar(255) DEFAULT NULL,
    `linkavt`  varchar(255) DEFAULT NULL,
    `phone`    varchar(255) DEFAULT NULL,
    `user_id`  int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKos8a39a6glhujvhfawobdii81` (`user_id`),
    CONSTRAINT `FKos8a39a6glhujvhfawobdii81` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_info`
--

LOCK TABLES `customer_info` WRITE;
/*!40000 ALTER TABLE `customer_info`
    DISABLE KEYS */;
INSERT INTO `customer_info`
VALUES (1, '2001-01-04', 'danh@gmail.com', 'Danh', NULL, '934567', 3),
       (2, '2001-01-04', 'kukietkaka105@gmail.com', 'Kiet Nguyen', '7d70a5e44c9e44369cab01b4b8ebbf8b.png', '966955940',
        4),
       (3, '2022-05-06', 'kukietkaka105@gmail.com', 'Kiet Nguyen', '5edafbfb53ea48e19f509e44c7ef4551.png', '966955940',
        5),
       (4, NULL, 'admin@web.home', 'admin', NULL, NULL, 1),
       (5, NULL, 'seller@web.home', 'seller', NULL, NULL, 2),
       (6, '2022-05-06', 'kiet@gmail.com', 'Kiet', '6e687c5550c84701802e6816adff1102.png', '0973274231', 6),
       (7, '2022-05-24', 'kiet@gmail.com', 'Kiet', '1c0e0a8c6a404850b54a2e9d94ccb2ff.png', '0973274231', 8),
       (8, '2022-05-14', 'thang@gmail.com', 'thang', 'c2bfa5bfb9b34b4eb146da19ab141939.png', '0973274231', 14),
       (9, '2022-05-21', 'thang@gmail.com', 'thang', '549febb32c024bf88ab14de23adcf38c.png', '0973274231', 11),
       (10, '2022-05-28', 'nghiem@gmail.com', 'nghiem', '7825c9a4c1b94235b5a025a295067825.png', '0973274231', 22);
/*!40000 ALTER TABLE `customer_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `product`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `amount`          int(11) NOT NULL,
    `name`            varchar(255) DEFAULT NULL,
    `price`           double  NOT NULL,
    `quantity`        int(11) NOT NULL,
    `thumbnail_photo` varchar(255) DEFAULT NULL,
    `category_id`     int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
    CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 46
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product`
    DISABLE KEYS */;
INSERT INTO `product`
VALUES (1, 0, 'Đồng hồ Jacques Lemans', 660, 20, '1_1_1.jpg', 1),
       (2, 0, 'Đồng Hồ EPOS SWISS', 545, 20, '1_2_1.jpg', 1),
       (3, 20, 'Đồng hồ Diamond D', 233, 20, '1_3_1.jpg', 1),
       (4, 20, 'ĐỒNG HỒ PHILIPPE AUGUSTE', 780, 20, '1_4_1.jpg', 1),
       (5, 0, 'Đồng hồ Atlantic Swiss', 749, 20, '1_5_1.jpg', 1),
       (6, 20, 'VÍ CẦM TAY PIERRE CARDIN', 449, 20, '2_6_1.jpg', 2),
       (7, 20, 'VÍ NAM PIERRE CARDIN', 850, 20, '2_7_1.jpg', 2),
       (8, 0, 'VÍ DA PIERRE CARDIN', 599, 20, '2_8_1.jpg', 2),
       (9, 20, 'VÍ CẦM TAY PIERRE CARDIN', 780, 20, '2_9_1.jpg', 2),
       (10, 0, 'VÍ DA PIERRE CARDIN', 749, 20, '2_10_1.jpg', 2),
       (11, 0, 'Ví Dài Meron', 449, 20, '3_11_1.jpg', 3),
       (12, 0, 'Ví Dài Da Sáp Smool-S', 850, 20, '3_12_1.jpg', 3),
       (13, 0, 'Ví Dài Mini - Rainbow', 599, 20, '3_13_1.jpg', 3),
       (14, 20, 'Ví Dài LOVE-L', 850, 20, '3_14_1.jpg', 3),
       (15, 20, 'SARK - Ví Dài', 599, 20, '3_15_1.jpg', 3),
       (16, 0, 'Balo Doanh Nhân', 780, 20, '4_16_1.jpg', 4),
       (17, 0, 'Balo Chống Sốc Laptop', 749, 20, '4_17_1.jpg', 4),
       (18, 0, 'Balo Du Lịch', 449, 20, '4_18_1.jpg', 4),
       (19, 20, 'Balo Du Lịch SimpleCarry', 850, 20, '4_19_1.jpg', 4),
       (20, 20, 'BALO DU LỊCH ĐA NĂNG', 599, 20, '4_20_1.jpg', 4),
       (21, 20, 'THẮT LƯNG RAF MÀU ĐEN', 599, 20, '5_21_1.jpg', 5),
       (22, 0, 'THẮT LƯNG \"THE ROLL\"', 449, 20, '5_22_1.png', 5),
       (23, 20, 'THẮT LƯNG RAF MÀU NÂU', 780, 20, '5_23_1.jpg', 5),
       (24, 0, 'THẮT LƯNG DAVID MÀU NÂU', 449, 20, '5_24_1.png', 5),
       (25, 0, 'THẮT LƯNG STEVEN (ĐEN)', 749, 20, '5_25_1.png', 5),
       (26, 20, 'GIÀY CONTINENTAL 80', 780, 20, '6_26_1.jpg', 6),
       (27, 0, 'GIÀY SUPERSTAR', 749, 20, '6_27_1.jpg', 6),
       (28, 0, 'GIÀY SUPERSTAR PRIDE', 449, 20, '6_28_1.jpg', 6),
       (29, 0, 'GIÀY STAN SMITH', 850, 20, '6_29_1.jpg', 6),
       (30, 20, 'GIÀY STAN SMITH PRIDE', 599, 20, '6_30_1.jpg', 6),
       (31, 0, 'Dép Nam Quai Ngang Da Bò', 780, 20, '7_31_1.jpg', 7),
       (32, 0, 'Dép Nam Quai Ngang SUPERSTAR', 749, 20, '7_32_1.jpg', 7),
       (33, 0, 'Dép Nam Quai Ngang', 449, 20, '7_33_1.jpg', 7),
       (34, 20, 'Dép xỏ ngón DUWA', 850, 20, '7_34_1.jpg', 7),
       (35, 0, 'Dép Nam Khủng Long', 599, 20, '7_35_1.jpg', 7),
       (36, 0, 'Tất Nam Vớ Nam Cổ Dài', 780, 20, '8_36_1.jpg', 8),
       (37, 0, 'Tất Nam Vớ Nam Cổ Ngắn', 749, 20, '8_37_1.jpg', 8),
       (38, 20, 'Tất Nam Vớ Nam Cổ Dài', 449, 20, '8_38_1.jpg', 8),
       (39, 20, 'Tất Nam Cổ Cao Caro', 850, 20, '8_39_1.jpg', 8),
       (40, 0, 'Tất Nam 5S Cổ Ngắn', 599, 20, '8_40_1.jpg', 8),
       (41, 0, 'Nón bucket', 780, 20, '9_41_1.jpg', 9),
       (42, 0, 'Nón tròn FREESIZE', 749, 20, '9_42_1.jpg', 9),
       (43, 20, 'Nón bucket', 449, 20, '9_43_1.jpg', 9),
       (44, 20, 'MLB -Like Ice', 850, 20, '9_44_1.jpg', 9),
       (45, 1, 'Nón Premi3r dadhat', 700, 20, '9_45_1.jpg', 9);
/*!40000 ALTER TABLE `product`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_detail`
--

DROP TABLE IF EXISTS `product_detail`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `product_detail`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `brand`       varchar(255) DEFAULT NULL,
    `color`       varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `material`    varchar(255) DEFAULT NULL,
    `product_id`  int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKilxoi77ctyin6jn9robktb16c` (`product_id`),
    CONSTRAINT `FKilxoi77ctyin6jn9robktb16c` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_detail`
--

LOCK TABLES `product_detail` WRITE;
/*!40000 ALTER TABLE `product_detail`
    DISABLE KEYS */;
INSERT INTO `product_detail`
VALUES (1, 'Hublob', 'Đen Xanh', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vàng',
        1),
       (2, 'Rolex', 'Trắng', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vàng', 2),
       (3, 'Rolex', 'Đen Xanh', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vàng',
        3),
       (4, 'Hublob', 'Đen Xanh', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vàng',
        4),
       (5, 'Nike', 'Trắng', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vải', 26),
       (6, 'Nike', 'Trắng', 'Mang lại vẻ sành điệu, lịch lãm và đầy nam tính cho người đàn ông hiện đại.', 'Vải', 27);
/*!40000 ALTER TABLE `product_detail`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `product_image`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `link`       varchar(255) DEFAULT NULL,
    `product_id` int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6oo0cvcdtb6qmwsga468uuukk` (`product_id`),
    CONSTRAINT `FK6oo0cvcdtb6qmwsga468uuukk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 181
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image`
    DISABLE KEYS */;
INSERT INTO `product_image`
VALUES (1, '1_1_1.jpg', 1),
       (2, '1_1_2.jpg', 1),
       (3, '1_1_3.jpg', 1),
       (4, '1_1_4.jpg', 1),
       (5, '1_2_1.jpg', 2),
       (6, '1_2_2.jpg', 2),
       (7, '1_2_3.jpg', 2),
       (8, '1_2_4.jpg', 2),
       (9, '1_3_1.jpg', 3),
       (10, '1_3_2.jpg', 3),
       (11, '1_3_3.jpg', 3),
       (12, '1_3_4.jpg', 3),
       (13, '1_4_1.jpg', 4),
       (14, '1_4_2.jpg', 4),
       (15, '1_4_3.jpg', 4),
       (16, '1_4_4.jpg', 4),
       (17, '1_5_1.jpg', 5),
       (18, '1_5_2.jpg', 5),
       (19, '1_5_3.jpg', 5),
       (20, '1_5_4.jpg', 5),
       (21, '2_6_1.jpg', 6),
       (22, '2_6_2.jpg', 6),
       (23, '2_6_3.jpg', 6),
       (24, '2_6_4.jpg', 6),
       (25, '2_7_1.jpg', 7),
       (26, '2_7_2.jpg', 7),
       (27, '2_7_3.jpg', 7),
       (28, '2_7_4.jpg', 7),
       (29, '2_8_1.jpg', 8),
       (30, '2_8_2.jpg', 8),
       (31, '2_8_3.jpg', 8),
       (32, '2_8_4.jpg', 8),
       (33, '2_9_1.jpg', 9),
       (34, '2_9_2.jpg', 9),
       (35, '2_9_3.jpg', 9),
       (36, '2_9_4.jpg', 9),
       (37, '2_10_1.jpg', 10),
       (38, '2_10_2.jpg', 10),
       (39, '2_10_3.jpg', 10),
       (40, '2_10_4.jpg', 10),
       (41, '3_11_1.jpg', 11),
       (42, '3_11_2.jpg', 11),
       (43, '3_11_3.jpg', 11),
       (44, '3_11_4.jpg', 11),
       (45, '3_12_1.jpg', 12),
       (46, '3_12_2.jpg', 12),
       (47, '3_12_3.jpg', 12),
       (48, '3_12_4.jpg', 12),
       (49, '3_13_1.jpg', 13),
       (50, '3_13_2.jpg', 13),
       (51, '3_13_3.jpg', 13),
       (52, '3_13_4.jpg', 13),
       (53, '3_14_1.jpg', 14),
       (54, '3_14_2.jpg', 14),
       (55, '3_14_3.jpg', 14),
       (56, '3_14_4.jpg', 14),
       (57, '3_15_1.jpg', 15),
       (58, '3_15_2.jpg', 15),
       (59, '3_15_3.jpg', 15),
       (60, '3_15_4.jpg', 15),
       (61, '4_16_1.jpg', 16),
       (62, '4_16_2.jpg', 16),
       (63, '4_16_3.jpg', 16),
       (64, '4_16_4.jpg', 16),
       (65, '4_17_1.jpg', 17),
       (66, '4_17_2.jpg', 17),
       (67, '4_17_3.jpg', 17),
       (68, '4_17_4.jpg', 17),
       (69, '4_18_1.jpg', 18),
       (70, '4_18_2.jpg', 18),
       (71, '4_18_3.jpg', 18),
       (72, '4_18_4.jpg', 18),
       (73, '4_19_1.jpg', 19),
       (74, '4_19_2.jpg', 19),
       (75, '4_19_3.jpg', 19),
       (76, '4_19_4.jpg', 19),
       (77, '4_20_1.jpg', 20),
       (78, '4_20_2.jpg', 20),
       (79, '4_20_3.jpg', 20),
       (80, '4_20_4.jpg', 20),
       (81, '5_21_1.jpg', 21),
       (82, '5_21_2.jpg', 21),
       (83, '5_21_3.jpg', 21),
       (84, '5_21_4.jpg', 21),
       (85, '5_22_1.png', 22),
       (86, '5_22_2.png', 22),
       (87, '5_22_3.png', 22),
       (88, '5_22_4.png', 22),
       (89, '5_23_1.jpg', 23),
       (90, '5_23_2.jpg', 23),
       (91, '5_23_3.jpg', 23),
       (92, '5_23_4.jpg', 23),
       (93, '5_24_1.png', 24),
       (94, '5_24_2.png', 24),
       (95, '5_24_3.png', 24),
       (96, '5_24_4.png', 24),
       (97, '5_25_1.png', 25),
       (98, '5_25_2.png', 25),
       (99, '5_25_3.png', 25),
       (100, '5_25_4.png', 25),
       (101, '6_26_1.jpg', 26),
       (102, '6_26_2.jpg', 26),
       (103, '6_26_3.jpg', 26),
       (104, '6_26_4.jpg', 26),
       (105, '6_27_1.jpg', 27),
       (106, '6_27_2.jpg', 27),
       (107, '6_27_3.jpg', 27),
       (108, '6_27_4.jpg', 27),
       (109, '6_28_1.jpg', 28),
       (110, '6_28_2.jpg', 28),
       (111, '6_28_3.jpg', 28),
       (112, '6_28_4.jpg', 28),
       (113, '6_29_1.jpg', 29),
       (114, '6_29_2.jpg', 29),
       (115, '6_29_3.jpg', 29),
       (116, '6_29_4.jpg', 29),
       (117, '6_30_1.jpg', 30),
       (118, '6_30_2.jpg', 30),
       (119, '6_30_3.jpg', 30),
       (120, '6_30_4.jpg', 30),
       (121, '7_31_1.jpg', 31),
       (122, '7_31_2.jpg', 31),
       (123, '7_31_3.jpg', 31),
       (124, '7_31_4.jpg', 31),
       (125, '7_32_1.jpg', 32),
       (126, '7_32_2.jpg', 32),
       (127, '7_32_3.jpg', 32),
       (128, '7_32_4.jpg', 32),
       (129, '7_33_1.jpg', 33),
       (130, '7_33_2.jpg', 33),
       (131, '7_33_3.jpg', 33),
       (132, '7_33_4.jpg', 33),
       (133, '7_34_1.jpg', 34),
       (134, '7_34_2.jpg', 34),
       (135, '7_34_3.jpg', 34),
       (136, '7_34_4.jpg', 34),
       (137, '7_35_1.jpg', 35),
       (138, '7_35_2.jpg', 35),
       (139, '7_35_3.jpg', 35),
       (140, '7_35_4.jpg', 35),
       (141, '8_36_1.jpg', 36),
       (142, '8_36_2.jpg', 36),
       (143, '8_36_3.jpg', 36),
       (144, '8_36_4.jpg', 36),
       (145, '8_37_1.jpg', 37),
       (146, '8_37_2.jpg', 37),
       (147, '8_37_3.jpg', 37),
       (148, '8_37_4.jpg', 37),
       (149, '8_38_1.jpg', 38),
       (150, '8_38_2.jpg', 38),
       (151, '8_38_3.jpg', 38),
       (152, '8_38_4.jpg', 38),
       (153, '8_39_1.jpg', 39),
       (154, '8_39_2.jpg', 39),
       (155, '8_39_3.jpg', 39),
       (156, '8_39_4.jpg', 39),
       (157, '8_40_1.jpg', 40),
       (158, '8_40_2.jpg', 40),
       (159, '8_40_3.jpg', 40),
       (160, '8_40_4.jpg', 40),
       (161, '9_41_1.jpg', 41),
       (162, '9_41_2.jpg', 41),
       (163, '9_41_3.jpg', 41),
       (164, '9_41_4.jpg', 41),
       (165, '9_42_1.jpg', 42),
       (166, '9_42_2.jpg', 42),
       (167, '9_42_3.jpg', 42),
       (168, '9_42_4.jpg', 42),
       (169, '9_43_1.jpg', 43),
       (170, '9_43_2.jpg', 43),
       (171, '9_43_3.jpg', 43),
       (172, '9_43_4.jpg', 43),
       (173, '9_44_1.jpg', 44),
       (174, '9_44_2.jpg', 44),
       (175, '9_44_3.jpg', 44),
       (176, '9_44_4.jpg', 44),
       (177, '9_45_1.jpg', 45),
       (178, '9_45_2.jpg', 45),
       (179, '9_45_3.jpg', 45),
       (180, '9_45_4.jpg', 45);
/*!40000 ALTER TABLE `product_image`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `user`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `pass_word` varchar(255) DEFAULT NULL,
    `role`      int(11) NOT NULL,
    `status`    int(11) NOT NULL,
    `user_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (1, 'admin', 3, 1, 'admin'),
       (2, 'seller', 2, 1, 'seller'),
       (3, 'danh123', 1, 0, 'danh123'),
       (4, 'ddanh123', 1, 1, 'key123'),
       (5, 'nghiemnguyen123', 1, 1, 'nghiem123'),
       (6, 'danh789', 1, 1, 'danh789'),
       (7, 'danh78911', 1, 1, 'danh789'),
       (8, 'nguyen190', 1, 1, 'nguyen190'),
       (9, 'ddanh1234', 1, 1, 'key1234'),
       (10, 'nghiemnguyen1234', 1, 1, 'nghiem1234'),
       (11, 'thangpham1234', 1, 1, 'thang1234'),
       (12, 'danhnguyen1235', 1, 1, 'key1235'),
       (13, 'nghiemnguyen1235', 1, 1, 'nghiem125'),
       (14, 'thangpham1235', 1, 1, 'thang125'),
       (15, 'danhnguyen1236', 1, 1, 'key126'),
       (16, 'nghiemnguyen1236', 1, 1, 'nghiem126'),
       (17, 'thangpham1236', 1, 1, 'thang126'),
       (18, 'nghiemnguyen1237', 1, 1, 'key1235'),
       (19, 'thangpham1237', 1, 1, 'nghiem125'),
       (20, 'danhnguyen1237', 1, 1, 'thang119'),
       (21, 'nghiemnguyen1238', 1, 1, 'key126'),
       (22, 'thangpham1238', 1, 1, 'nghiem126'),
       (23, 'nghiemnguyen1238', 1, 1, 'thang126');
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;


