/*
SQLyog Ultimate v11.42 (64 bit)
MySQL - 5.5.43-0ubuntu0.14.04.1 : Database - messages
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`messages` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `messages`;

/*Table structure for table `application` */

DROP TABLE IF EXISTS `application`;

CREATE TABLE `application` (
  `app_id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(100) NOT NULL,
  `app_key` varchar(50) NOT NULL,
  KEY `app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT,
  `msg_text` text NOT NULL,
  `msg_phone_numbers` text NOT NULL,
  `msg_sender_id` int(30) NOT NULL,
  `msg_sender_name` varchar(30) NOT NULL,
  `msg_status` enum('SEND','SEND_TO_PHONE','NOT_SEND','QUOTE(MAX_COUNT)_EXCEED','QUOTE(SAME_MESSAGE)_EXCEED') NOT NULL,
  `msg_comment` text NOT NULL,
  `msg_creation_date` datetime NOT NULL,
  `msg_send_to_phone_date` datetime DEFAULT NULL,
  `msg_send_date` datetime DEFAULT NULL,
  `msg_app_id` int(11) NOT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `quote` */

DROP TABLE IF EXISTS `quote`;

CREATE TABLE `quote` (
  `qt_app_id` int(11) NOT NULL,
  `qt_max_per_hour` int(20) NOT NULL,
  `qt_same_min_interval` int(20) NOT NULL COMMENT 'интервал задается в минутах'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sender` */

DROP TABLE IF EXISTS `sender`;

CREATE TABLE `sender` (
  `sd_id` int(11) NOT NULL AUTO_INCREMENT,
  `sd_name` varchar(30) NOT NULL,
  PRIMARY KEY (`sd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sender_phone` */

DROP TABLE IF EXISTS `sender_phone`;

CREATE TABLE `sender_phone` (
  `sdp_sender_id` int(11) NOT NULL,
  `sdp_imei` varchar(15) NOT NULL,
  `sdp_password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
