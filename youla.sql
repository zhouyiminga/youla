/*
Navicat MySQL Data Transfer

Date: 2023-12-14 05:30:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `articles`
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `body` text,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `index_case_name` (`title`,`body`) /*!50100 WITH PARSER `ngram` */ 
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of articles
-- ----------------------------

-- ----------------------------
-- Table structure for `params`
-- ----------------------------
DROP TABLE IF EXISTS `params`;
CREATE TABLE `params` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `paramName` varchar(32) NOT NULL,
  `paramValue` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of params
-- ----------------------------

-- ----------------------------
-- Table structure for `picstore`
-- ----------------------------
DROP TABLE IF EXISTS `picstore`;
CREATE TABLE `picstore` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `filePath` varchar(64) NOT NULL,
  `pictype` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1188 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of picstore
-- ----------------------------



-- ----------------------------
-- Table structure for `roleinfo`
-- ----------------------------
DROP TABLE IF EXISTS `roleinfo`;
CREATE TABLE `roleinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(32) DEFAULT NULL,
  `roleParentName` varchar(32) DEFAULT NULL,
  `roleType` varchar(8) DEFAULT NULL,
  `imagePath` varchar(50) DEFAULT NULL,
  `reason` varchar(50) DEFAULT NULL,
  `area` varchar(16) DEFAULT NULL,
  `bigArea` varchar(32) DEFAULT NULL,
  `littleArea` varchar(32) DEFAULT NULL,
  `memo` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of roleinfo
-- ----------------------------


-- ----------------------------
-- Table structure for `roletype`
-- ----------------------------
DROP TABLE IF EXISTS `roletype`;
CREATE TABLE `roletype` (
  `id` int(11) NOT NULL,
  `typeName` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of roletype
-- ----------------------------
-- ----------------------------
-- Table structure for `seq_folder`
-- ----------------------------
DROP TABLE IF EXISTS `seq_folder`;
CREATE TABLE `seq_folder` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `nullValue` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=609 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of seq_folder
-- ----------------------------
