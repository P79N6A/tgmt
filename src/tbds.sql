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

CREATE TABLE `tbds`.`tbds_mps` (
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
