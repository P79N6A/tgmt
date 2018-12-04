/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  totan
 * Created: 2018-11-24
 */

CREATE SCHEMA `tbds` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `joc`.`tbds_mps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fullname` VARCHAR(100) NULL,
  `desc` VARCHAR(255) NULL,
  `host_ip` VARCHAR(255) NULL,
  `host_port` VARCHAR(5) NULL,
  `train_type` VARCHAR(50) NULL,
  `train_num` VARCHAR(50) NULL,
  `ab_marker` VARCHAR(4) NULL,
  `status` INT NULL,
  `client_state_file` VARCHAR(500) NULL,
  `client_state_log` VARCHAR(500) NULL,
  PRIMARY KEY (`id`));
  
  /*
-- Query: SELECT * FROM joc.tbds_mps
LIMIT 0, 1000

-- Date: 2018-11-26 16:05
*/
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (1,'ATP_0223','ATP_0223A','127.0.0.1','2222','ATP','0223','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (2,'ATP_0223','ATP_0223B','192.168.0.100','2222','ATP','0223','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (3,'ATP_0224','ATP_0224A','172.17.0.1','2222','ATP','0224','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (4,'ATP_0224','ATP_0224B','172.17.0.2','2222','ATP','0224','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (5,'ATP_0225','ATP_0225A','172.17.0.3','2222','ATP','0225','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (6,'ATP_0225','ATP_0225B','172.17.0.4','2222','ATP','0225','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (7,'ATP_0226','ATP_0226A','192.168.0.101','2222','ATP','0226','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (8,'ATP_0226','ATP_0226B','192.168.0.102','2222','ATP','0226','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (9,'ATP_0227','ATP_0227A','192.168.0.103','2222','ATP','0227','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (10,'ATP_0227','ATP_0227B','192.168.0.104','2222','ATP','0227','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (11,'ATP_0228','ATP_0228A','192.168.0.105','2222','ATP','0228','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (12,'ATP_0228','ATP_0228B','192.168.0.106','2222','ATP','0228','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (13,'ATP_0229','ATP_0229A','192.168.0.107','2222','ATP','0229','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (14,'ATP_0229','ATP_0229B','192.168.0.108','2222','ATP','0229','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (15,'ATP_0230','ATP_0230A','192.168.0.109','2222','ATP','0230','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (16,'ATP_0230','ATP_0230B','192.168.0.110','2222','ATP','0230','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (17,'ATP_0231','ATP_0231A','192.168.0.111','2222','ATP','0231','A',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');
INSERT INTO `tbds_mps` (`id`,`fullname`,`desc`,`host_ip`,`host_port`,`train_type`,`train_num`,`ab_marker`,`status`,`client_state_file`,`client_state_log`) VALUES (18,'ATP_0231','ATP_0231B','192.168.0.112','2222','ATP','0231','B',1,'C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log','C:/Users/totan/Documents/NetBeansProjects/tbdsweb/log');

CREATE TABLE `joc`.`tbds_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL, --
  `description` VARCHAR(500) NULL,
  `flag` VARCHAR(1) NULL,
  `created` DATETIME NULL,
  `modified` DATETIME NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE `joc`.`tbds_permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `action_key` VARCHAR(500) NULL,
  `node` VARCHAR(500) NULL,
  `type` VARCHAR(45) NULL,
  `text` VARCHAR(500) NULL,
  `created` DATETIME NULL,
  `modified` DATETIME NULL,
  PRIMARY KEY (`id`));

  
  CREATE TABLE `joc`.`tbds_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NULL, --用户名
  `nickname` VARCHAR(255) NULL, --昵称
  `password` VARCHAR(500) NULL, --密码
  `salt` VARCHAR(500) NULL, -- 加密盐
  `email` VARCHAR(500) NULL, -- 邮箱
  `mobile` VARCHAR(20) NULL, -- 手机
  `gender` VARCHAR(1) NULL, -- 性别
  `birthday` DATE NULL, -- 生日
  `company` VARCHAR(1000) NULL, -- 公司
  `status` VARCHAR(1) NULL, -- 用户账号状态
  `logged` DATETIME NULL, -- 用户最近的登录时间
  `activated` DATETIME NULL, -- 用户激活时间
  `created` DATETIME NULL, -- 用户创建时间
  PRIMARY KEY (`id`));
  
  
/*
-- Query: SELECT * FROM joc.tbds_user
LIMIT 0, 1000

-- Date: 2018-12-03 17:45
*/
INSERT INTO `tbds_user` (`id`,`username`,`nickname`,`password`,`salt`,`email`,`mobile`,`gender`,`birthday`,`company`,`status`,`logged`,`activated`,`created`,`modified`) VALUES (1,'admin','admin','ae6f78da52c3f7e7b8afa7bbcaabd351b49895b8ac4fa4a8860ea4ce9bfe286c','Gtp_z4YYzaR69TUXEOlXzjCFV6wAeHAC','admin@tbds.com',NULL,'1','2018-12-01','智铁科技','1',NULL,'2018-12-03 10:54:35','2018-12-03 10:54:35','2018-12-03 16:36:56');
INSERT INTO `tbds_user` (`id`,`username`,`nickname`,`password`,`salt`,`email`,`mobile`,`gender`,`birthday`,`company`,`status`,`logged`,`activated`,`created`,`modified`) VALUES (3,'joe','红波','c50df3e3a2b8f92cd8ff0919a4a558e4639ba5bfa7c0e173b113656aaa8c67ef','5fa6jgDBKqTvXMwm9fh4HL8G7FdCiJw2','joe@tbds.com',NULL,'1','2018-12-01','智铁科技','1',NULL,'2018-12-03 11:44:33','2018-12-03 11:44:33','2018-12-03 17:34:32');
INSERT INTO `tbds_user` (`id`,`username`,`nickname`,`password`,`salt`,`email`,`mobile`,`gender`,`birthday`,`company`,`status`,`logged`,`activated`,`created`,`modified`) VALUES (4,'tommy','tommy',NULL,NULL,'tommy@tbds.com',NULL,'1','2018-12-02',NULL,'0',NULL,NULL,'2018-12-03 16:53:04','2018-12-03 16:53:04');

