/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50644
Source Database       : merchants

Target Server Type    : MYSQL
Target Server Version : 50644
File Encoding         : 65001

Date: 2021-02-14 10:50:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for merchants
-- ----------------------------
DROP TABLE IF EXISTS `merchants`;
CREATE TABLE `merchants` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 NOT NULL COMMENT '商户名称',
  `logo_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户 logo',
  `business_license_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户营业执照',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户联系电话',
  `address` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户地址',
  `is_audit` tinyint(1) NOT NULL COMMENT '是否通过审核',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
