/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50627
Source Host           : localhost:3306
Source Database       : amazon

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2016-07-26 16:30:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for price_history
-- ----------------------------
DROP TABLE IF EXISTS `price_history`;
CREATE TABLE `price_history` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` varchar(64) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `price` decimal(10,2) DEFAULT NULL,
  `global_ship` char(1) DEFAULT NULL COMMENT '是否直邮到中国',
  `ship_fee` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of price_history
-- ----------------------------
INSERT INTO `price_history` VALUES ('29', 'B00IE3ELIW', '2016-07-26 15:53:15', '29.99', 'N', '0.00');
INSERT INTO `price_history` VALUES ('30', 'B011EG5HJ2', '2016-07-26 15:53:16', '12.59', 'Y', '6.21');
INSERT INTO `price_history` VALUES ('31', 'B00FBWFVK6', '2016-07-26 15:53:17', '26.25', 'N', '0.00');
INSERT INTO `price_history` VALUES ('32', 'B0053FXGUI', '2016-07-26 15:53:17', '14.99', 'Y', '8.66');
INSERT INTO `price_history` VALUES ('33', 'B00TV0T0HY', '2016-07-26 15:53:17', '45.58', 'Y', '6.86');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` varchar(64) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `image` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('B011EG5HJ2', 'View-Master Virtual Reality Starter Pack', 'http://www.view-master.com/en-US/Images/ViewerStarterPack_tcm1147-251976.jpg');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(512) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'javyuan', 'javyuan', 'yuanjifeng1@qq.com', '18501364095');

-- ----------------------------
-- Table structure for user_product
-- ----------------------------
DROP TABLE IF EXISTS `user_product`;
CREATE TABLE `user_product` (
  `id` int(64) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(64) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `remind_price` decimal(10,0) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_product
-- ----------------------------
INSERT INTO `user_product` VALUES ('1', '1', 'B011EG5HJ2', '0', '2016-07-25 00:00:00', '0');
INSERT INTO `user_product` VALUES ('2', '1', 'B0053FXGUI', '0', '2016-07-26 00:00:00', '0');
INSERT INTO `user_product` VALUES ('3', '1', 'B0053FXGE4', '0', '2016-07-26 00:00:00', '0');
INSERT INTO `user_product` VALUES ('4', '1', 'B00IE3ELIW', '0', '2016-07-26 00:00:00', '0');
INSERT INTO `user_product` VALUES ('5', '1', 'B00TV0T0HY', '0', '2016-07-26 00:00:00', '0');
INSERT INTO `user_product` VALUES ('6', '1', 'B00FBWFVK6', '0', '2016-07-26 00:00:00', '0');
