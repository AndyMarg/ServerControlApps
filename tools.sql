-- Комментарий
/*
SQLyog Ultimate v11.42 (64 bit)
MySQL - 5.5.41-0ubuntu0.14.04.1 : Database - tools
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`tools` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `tools`;

/*Table structure for table `CHANGELOG` */

DROP TABLE IF EXISTS `CHANGELOG`;

CREATE TABLE `CHANGELOG` (
  `ID` decimal(20,0) NOT NULL,
  `APPLIED_AT` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `CHANGELOG` */

/*Table structure for table `PAC_server_state` */

DROP TABLE IF EXISTS `PAC_server_state`;

CREATE TABLE `PAC_server_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_server` int(11) NOT NULL,
  `state` int(11) NOT NULL COMMENT '0 - dead, 1 - alive',
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_server_state_1` (`id_server`,`time`),
  CONSTRAINT `fk_server_state_1` FOREIGN KEY (`id_server`) REFERENCES `PAC_servers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `PAC_server_state` */

/*Table structure for table `PAC_servers` */

DROP TABLE IF EXISTS `PAC_servers`;

CREATE TABLE `PAC_servers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` int(11) NOT NULL,
  `isActive` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `PAC_servers` */

/*Table structure for table `SC_settings` */

DROP TABLE IF EXISTS `SC_settings`;

CREATE TABLE `SC_settings` (
  `component` varchar(45) NOT NULL COMMENT 'название компонента',
  `name` varchar(45) NOT NULL COMMENT 'название настройки',
  `value` text NOT NULL COMMENT 'значение настройки'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `SC_settings` */

insert  into `SC_settings`(`component`,`name`,`value`) values ('mail','password','hysteria52'),('mail','login','alisa.margashova@vistar.su'),('mail','adress_to','alisa.margashova@yandex.ru,alisa.margashova@vistar.su'),('mail','adress_from','alisa.margashova@vistar.su'),('mail','smtpPort','587'),('mail','stmpHost','smtp.yandex.ru'),('shadowClient','server_adress','marshrut.vistar.su'),('shadowClient','port','20997'),('shadowClient','key','0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
