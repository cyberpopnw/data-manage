/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 192.168.109.201:13306
 Source Schema         : cyber_business

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 11/04/2022 22:55:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cyber_buried_point
-- ----------------------------
DROP TABLE IF EXISTS `cyber_buried_point`;
CREATE TABLE `cyber_buried_point`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '用户Id',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求ip地址',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求url',
  `method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Ip来源',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cyber_buried_point
-- ----------------------------
INSERT INTO `cyber_buried_point` VALUES (1, 1, '10.8.0.4', '/user/auth', 'POST', 'loginUserDto={publicAddress: 0x9215a6bf728d717f3a3eb1ade9d1e1111c3584df, signature: 0x2e204c3d963550c8ffbbcab22d95c3ae1b10b4fabbb9155721dd6875789fd8901743a9a5f50c48d9117641f52df7847412fabc76a5d050f9ef8fe9968382ddeb1c, nonce: 2022041122461280T9, inviterCode: x9pfRl}', '内网IP|内网IP', '2022-04-11 22:46:30');
INSERT INTO `cyber_buried_point` VALUES (2, 2, '10.8.0.4', '/user/auth', 'POST', 'loginUserDto={publicAddress: 0x40bad0c335a5d95965333957756e90e83ba68d91, signature: 0x1d06b817164d243c034b9eaec4ca05bd705670e87c2c47eb0a070cd42f77567b23605930a1b1414b78b96d97274d057695ebdaa84ea7cd575b9fcf41723ac9631c, nonce: 202204112250267G09, inviterCode: x9pfRl}', '内网IP|内网IP', '2022-04-11 22:50:34');
INSERT INTO `cyber_buried_point` VALUES (3, 1, '10.8.0.4', '/user/auth', 'POST', 'loginUserDto={publicAddress: 0x9215a6bf728d717f3a3eb1ade9d1e1111c3584df, signature: 0x61a20c6d928f3137a3cdb92c0efa98163a442d61eb3a868021cc2ea2db27fd477d36bc809dc2aa313b6cf7d537ae05d64adf69f83becd991e0de5fac5f5e8a6a1c, nonce: 202204112251122D89, inviterCode: x9pfRl}', '内网IP|内网IP', '2022-04-11 22:51:17');

-- ----------------------------
-- Table structure for cyber_inviter
-- ----------------------------
DROP TABLE IF EXISTS `cyber_inviter`;
CREATE TABLE `cyber_inviter`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '用户Id',
  `inviter_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `level` tinyint(1) NULL DEFAULT 2 COMMENT '经销商等级：1:国家级 2:地区级',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `one`(`inviter_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cyber_inviter
-- ----------------------------
INSERT INTO `cyber_inviter` VALUES (1, 1, 'x9pfRl', 2, '2022-04-09 23:15:21', NULL);

-- ----------------------------
-- Table structure for cyber_user
-- ----------------------------
DROP TABLE IF EXISTS `cyber_user`;
CREATE TABLE `cyber_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '钱包地址',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `nonce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 0:封禁 1:正常',
  `role` tinyint(1) NULL DEFAULT 1 COMMENT '角色 1:普通用户 2:经销商 3:其他',
  `inviter_id` bigint(20) NULL DEFAULT NULL COMMENT '经销商id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `one`(`addr`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cyber_user
-- ----------------------------
INSERT INTO `cyber_user` VALUES (1, '0x9215a6bf728d717f3a3eb1ade9d1e1111c3584df', '11', '202204112251122D89', 1, 1, NULL, '2022-04-09 23:15:21', '2022-04-11 22:51:17');
INSERT INTO `cyber_user` VALUES (2, '0x40bad0c335a5d95965333957756e90e83ba68d91', NULL, '202204112250267G09', 1, 1, 1, '2022-04-11 22:50:26', '2022-04-11 22:50:33');

SET FOREIGN_KEY_CHECKS = 1;
