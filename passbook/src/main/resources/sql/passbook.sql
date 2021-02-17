/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50644
Source Database       : passbook

Target Server Type    : MYSQL
Target Server Version : 50644
File Encoding         : 65001

Date: 2021-02-14 10:55:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1.优惠券评论 2.app评论',
  `templateId` bigint(20) NOT NULL DEFAULT '-1',
  `comment` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for pass
-- ----------------------------
DROP TABLE IF EXISTS `pass`;
CREATE TABLE `pass` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `templateId` bigint(20) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `assignedDate` datetime NOT NULL,
  `conDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for passtemplate
-- ----------------------------
DROP TABLE IF EXISTS `passtemplate`;
CREATE TABLE `passtemplate` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT,
  `merchantId` int(11) NOT NULL,
  `goodsId` bigint(20) NOT NULL,
  `discount` int(11) NOT NULL,
  `title` varchar(30) CHARACTER SET utf8 NOT NULL,
  `summary` varchar(100) CHARACTER SET utf8 NOT NULL,
  `desc` varchar(300) CHARACTER SET utf8 NOT NULL,
  `limit` int(11) NOT NULL,
  `hasToken` tinyint(1) NOT NULL,
  `background` int(1) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `is_audit` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否通过审核（1.通过，0不通过）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
