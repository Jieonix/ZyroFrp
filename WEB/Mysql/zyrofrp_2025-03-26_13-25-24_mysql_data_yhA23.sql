-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: localhost    Database: zyrofrp
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `announcements`
--

DROP TABLE IF EXISTS `announcements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `content` text COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_pinned` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcements`
--

LOCK TABLES `announcements` WRITE;
/*!40000 ALTER TABLE `announcements` DISABLE KEYS */;
INSERT INTO `announcements` VALUES (1,'FRP节点免费开放，持续更新中！','亲爱的用户，感谢您选择我们的FRP服务！目前，我们网站正在免费提供服务，所有的节点都将持续开放，旨在为用户提供稳定且高速的网络体验。我们会持续优化服务，确保您享受更好的使用体验。敬请期待更多更新！','2025-03-26 09:03:02','2025-03-26 09:03:02',1),(2,'主页广告位招商进行中！','我们现已开启主页广告位招商！目前广告位每月仅需20元，机会难得，欢迎有兴趣的商家联系我们！广告合作将为双方带来更多曝光机会，共同拓展市场。','2025-03-26 09:03:30','2025-03-26 09:03:30',0),(3,'遇到问题？欢迎反馈！','如果在使用过程中遇到任何问题，或发现BUG，欢迎加入我们的QQ群：738146595进行反馈。我们将第一时间处理并优化服务，感谢您的支持与理解！','2025-03-26 09:03:38','2025-03-26 09:03:38',0),(4,'更多功能与增值服务即将推出！','我们正在努力开发更多新功能和增值服务，以便为您提供更好的体验。未来，我们将推出更多的优化和服务，敬请期待！感谢您的支持，未来我们将继续为大家提供更好的服务。','2025-03-26 09:03:50','2025-03-26 09:03:50',0),(5,'账户安全提醒','请妥善保管您的Token和账户信息，避免泄露给他人。如果发现异常使用情况，请尽快修改您的账户信息，并联系我们进行处理。','2025-03-26 09:50:24','2025-03-26 09:50:24',0),(6,'FRP服务使用须知','请确保您的隧道配置符合规范，避免滥用服务。如发现违规使用（如攻击、违法用途等），我们有权暂停或封禁相关账户。让我们共同维护一个良好的使用环境！','2025-03-26 09:50:47','2025-03-26 09:50:47',0),(7,'每日答题，领取超高流量奖励！','签到页面新增每日答题活动，答对答错皆可获得高额流量奖励！题目超级简单，人人都能参与，还等什么？快来试试吧！','2025-03-26 10:04:20','2025-03-26 10:10:28',1),(8,'FRP服务仍在开发完善，持续优化中！','目前，我们的FRP出租服务仍处于开发完善阶段，正在不断优化功能和提升稳定性。如果您在使用过程中遇到任何问题或有建议，欢迎加入QQ群：738146595 进行反馈。您的意见对我们非常重要，我们将持续改进，为大家提供更优质的服务！','2025-03-26 10:00:05','2025-03-26 10:05:41',1),(9,'网站由个人独立开发，持续优化中！','本网站所有功能均由Zyro团队独立开发，未使用任何第三方商业系统，代码均为原创。','2025-03-26 09:56:39','2025-03-26 09:56:39',0),(10,'用爱发电！','我们承诺，本站的 FRP 免费节点将长期存在，不会随意收费。虽然未来可能会增加一些增值服务，但免费的基础功能不会受影响。请大家放心使用，如有任何疑问，欢迎在QQ群：738146595 交流。','2025-03-26 10:02:46','2025-03-26 10:13:18',1),(11,'实名认证声明','为了保障服务的稳定性与安全性，本网站已启用实名认证机制。用户需完成实名认证后，方可使用 FRP 隧道服务。\n\n实名认证流程简便，仅需一步即可完成。请前往身份认证页面完成认证，即可正常使用服务。\n\n如有疑问或遇到问题，请加入QQ群：738146595 进行咨询。','2025-03-26 10:03:23','2025-03-26 10:08:30',1);
/*!40000 ALTER TABLE `announcements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email`
--

DROP TABLE IF EXISTS `email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `code` varchar(10) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `expired_at` datetime(6) NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email`
--

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;
INSERT INTO `email` VALUES (1,'2040804972@qq.com','1288','2025-03-19 02:31:34.714649','2025-03-19 02:36:34.714713',1,'REGISTER'),(2,'2040804972@qq.com','7870','2025-03-19 03:09:28.617298','2025-03-19 03:14:28.617386',1,'REGISTER'),(3,'2040804972@qq.com','3902','2025-03-19 03:14:26.319695','2025-03-19 03:19:26.319729',1,'REGISTER'),(4,'2040804972@qq.com','5742','2025-03-19 03:17:25.623230','2025-03-19 03:22:25.623305',1,'REGISTER'),(5,'2040804972@qq.com','3102','2025-03-21 20:46:33.546658','2025-03-21 20:51:33.546792',-1,'REGISTER'),(6,'2040804972@qq.com','5506','2025-03-26 11:03:31.450450','2025-03-26 11:08:31.450622',-2,'REGISTER'),(7,'2040804972@qq.com','8722','2025-03-26 11:04:30.201438','2025-03-26 11:09:30.201605',-2,'REGISTER'),(8,'2040804972@qq.com','3648','2025-03-26 11:04:42.441605','2025-03-26 11:09:42.441836',-1,'REGISTER');
/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frp_tunnels`
--

DROP TABLE IF EXISTS `frp_tunnels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frp_tunnels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `user_key` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `server_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `tunnel_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `tunnel_type` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `local_ip` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `local_port` int NOT NULL,
  `remote_port` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `server_ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `custom_domain` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `secret_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `server_name_2` (`server_name`,`tunnel_name`),
  UNIQUE KEY `server_name` (`server_name`,`remote_port`),
  CONSTRAINT `fk_server_name` FOREIGN KEY (`server_name`) REFERENCES `servers` (`name`),
  CONSTRAINT `chk_custom_domain_http_https` CHECK ((((`tunnel_type` in (_utf8mb4'http',_utf8mb4'https')) and (`custom_domain` is not null)) or ((`tunnel_type` not in (_utf8mb4'http',_utf8mb4'https')) and (`custom_domain` is null)))),
  CONSTRAINT `chk_secret_key_xtcp_stcp` CHECK ((((`tunnel_type` in (_utf8mb4'xtcp',_utf8mb4'stcp')) and (`secret_key` is not null)) or ((`tunnel_type` not in (_utf8mb4'xtcp',_utf8mb4'stcp')) and (`secret_key` is null)))),
  CONSTRAINT `frp_tunnels_chk_1` CHECK ((`local_port` < 65535)),
  CONSTRAINT `frp_tunnels_chk_2` CHECK (((`remote_port` >= 10000) and (`remote_port` < 65535)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frp_tunnels`
--

LOCK TABLES `frp_tunnels` WRITE;
/*!40000 ALTER TABLE `frp_tunnels` DISABLE KEYS */;
/*!40000 ALTER TABLE `frp_tunnels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `option_a` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `option_b` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `option_c` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `option_d` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `correct_answer` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'1+1等于多少？','1','2','3','4','B','2025-03-20 01:30:49'),(2,'太阳是哪个星球的中心？','地球','火星','太阳','木星','C','2025-03-20 01:30:58'),(3,'哪个颜色是天空的颜色？','红色','蓝色','绿色','黄色','B','2025-03-20 01:31:04'),(5,'水的化学式是什么？','CO2','H2O','O2','CH4','B','2025-03-20 01:32:01'),(6,'猫是何种动物？','鸟类','鱼类','哺乳动物','昆虫','C','2025-03-20 01:32:06'),(7,'中国的国旗是什么颜色为主？','红色','蓝色','绿色','黄色','A','2025-03-20 01:32:10'),(8,'一年有多少个月？','10','12','14','16','B','2025-03-20 01:32:14'),(9,'哪个动物会飞？','狗','猫','鸟','猪','C','2025-03-20 01:32:18'),(10,'1的平方是多少？','1','2','3','4','A','2025-03-20 01:32:22'),(11,'哪个是热带水果？','苹果','香蕉','葡萄','草莓','B','2025-03-20 01:32:26');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servers`
--

DROP TABLE IF EXISTS `servers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` int NOT NULL DEFAULT '7788',
  `max_clients` int DEFAULT '100',
  `current_clients` int DEFAULT '0',
  `vip_only` tinyint(1) DEFAULT '0',
  `status` enum('ONLINE','OFFLINE') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'ONLINE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servers`
--

LOCK TABLES `servers` WRITE;
/*!40000 ALTER TABLE `servers` DISABLE KEYS */;
INSERT INTO `servers` VALUES (1,'香港1号','hk1.zyroo.cn',7788,150,0,0,'ONLINE','2025-03-19 10:37:06','2025-03-24 11:56:44');
/*!40000 ALTER TABLE `servers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_answers`
--

DROP TABLE IF EXISTS `user_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_answers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `question_id` bigint NOT NULL,
  `user_answer` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  `answered_at` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`,`answered_at`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_answers`
--

LOCK TABLES `user_answers` WRITE;
/*!40000 ALTER TABLE `user_answers` DISABLE KEYS */;
INSERT INTO `user_answers` VALUES (2,'2040804972@qq.com',10,'A',1,'2025-03-21'),(3,'2040804972@qq.com',1,'B',1,'2025-03-22'),(4,'2040804972@qq.com',3,'B',1,'2025-03-24'),(5,'2040804972@qq.com',5,'B',1,'2025-03-26');
/*!40000 ALTER TABLE `user_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `vip_status` int NOT NULL DEFAULT '0',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `remaining_traffic` bigint NOT NULL,
  `upload_limit` bigint NOT NULL,
  `download_limit` bigint NOT NULL,
  `real_name_status` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `real_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_card` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_key` (`user_key`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10000,'4ii057kne8u8jice2iva','2040804972@qq.com','$2a$10$sBP5GanxBufLO8g4PtxiJ.WgR5ZtmDfYjjHxZKT/bVEz90ILGldsi',0,'USER','2025-03-18 19:17:42','2025-03-18 19:17:42',4454,1000,1000,'1','沙杰','HZIGl5j+UexIV+vzk13LDKptRBTgN+bvZ4pzNTpvyGc=');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'zyrofrp'
--

--
-- Dumping routines for database 'zyrofrp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-26 13:25:24
