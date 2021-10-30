/*
 Navicat Premium Data Transfer

 Source Server         : rfid
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 192.168.0.150:3306
 Source Schema         : rfid_check

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 30/10/2021 17:40:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for produ_bind
-- ----------------------------
DROP TABLE IF EXISTS `produ_bind`;
CREATE TABLE `produ_bind`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seq` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上料顺序',
  `no_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单号NO',
  `produ_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '零件代号',
  `work_cell_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工位编码',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `bar_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码数据',
  `rfid_uid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'RFID主键',
  `rfid_data` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'RFID数据',
  `bind_state` int NOT NULL DEFAULT 0 COMMENT '绑定状态（0未绑定1已绑定）',
  `bind_type` int NULL DEFAULT NULL COMMENT '绑定类型（0手工1扫码）',
  `bind_date` date NULL DEFAULT NULL COMMENT '绑定时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of produ_bind
-- ----------------------------
INSERT INTO `produ_bind` VALUES ('b93ef4f5acde4a7e98603eae18fd0f69', '1', '20211028001', '20211028001', NULL, '2021-10-28 23:34:03', NULL, NULL, NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for produ_check
-- ----------------------------
DROP TABLE IF EXISTS `produ_check`;
CREATE TABLE `produ_check`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `produ_bind_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定主键',
  `check_state` int NULL DEFAULT NULL COMMENT '校验状态（0未校验1已校验）',
  `check_result` int NULL DEFAULT NULL COMMENT '效验结果（0不通过1合格）',
  `check_date` date NULL DEFAULT NULL COMMENT '校验时间',
  `check_produ_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读取零件编码\r\n',
  `check_rfid_uid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读取RFIDUID',
  `check_work_cell` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `check_rfid_data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of produ_check
-- ----------------------------

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `equip_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机床编号',
  `produ_lot_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '零件批次号',
  `qty` int NULL DEFAULT 0 COMMENT '总数量',
  `bar_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码数据',
  `bind_type` int NULL DEFAULT NULL COMMENT '绑定类型（0手工1扫码）',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('5e787967fc8a422cb0d36a4a5fffb74a', '1', ' 62108017>CT0299.0020', 10, NULL, NULL, '2021-10-30 17:19:00');
INSERT INTO `task` VALUES ('ad15813a53f944f08a7fa073bd122951', '2', ' 62108017>CT0299.0021', 10, NULL, NULL, '2021-10-30 17:20:18');

-- ----------------------------
-- Table structure for task_bind
-- ----------------------------
DROP TABLE IF EXISTS `task_bind`;
CREATE TABLE `task_bind`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rfid_uid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'RFID主键',
  `rfid_data` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'RFID数据',
  `task_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `check_state` int NULL DEFAULT 0 COMMENT '检测状态（0未检测，1已检测）',
  `check_date` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `check_message` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检测消息',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_bind
-- ----------------------------
INSERT INTO `task_bind` VALUES ('4e963758998f44ea836279a9fea657ef', '309830CE500104E0', '', '5e787967fc8a422cb0d36a4a5fffb74a', 1, '2021-10-30 17:35:49', '校验通过', '2021-10-30 17:19:40');
INSERT INTO `task_bind` VALUES ('51d57f6ac7644b97b889430acc155d7c', 'F48E30CE500104E0', '', 'ad15813a53f944f08a7fa073bd122951', 1, '2021-10-30 17:34:38', '校验通过', '2021-10-30 17:20:29');
INSERT INTO `task_bind` VALUES ('5c8ddb7690584626b1a9f20d09130357', 'E78230CE500104E0', '', '5e787967fc8a422cb0d36a4a5fffb74a', 1, '2021-10-30 17:36:25', '校验通过', '2021-10-30 17:19:16');

-- ----------------------------
-- Table structure for task_log
-- ----------------------------
DROP TABLE IF EXISTS `task_log`;
CREATE TABLE `task_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '\r\n名称',
  `flag` int NULL DEFAULT NULL COMMENT '状态（0失败1成功）',
  `data` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据',
  `message` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果',
  `request_date` date NULL DEFAULT NULL COMMENT '请求时间',
  `response_date` date NULL DEFAULT NULL COMMENT '返回时间',
  `create_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_log
-- ----------------------------
INSERT INTO `task_log` VALUES ('012f568964574a3e87a2bd82954c3938', '校验RFID信息', 1, '1', '校验通过', '2021-10-30', '2021-10-30', NULL);
INSERT INTO `task_log` VALUES ('518f299d2e7b43d98f3a7723e46f5d71', '校验RFID信息', 1, '1', '校验通过', '2021-10-30', '2021-10-30', NULL);
INSERT INTO `task_log` VALUES ('6d8538d672cd47c9b7ca1ee737720b80', '校验RFID信息', 0, '2', '机床与绑定不匹配', '2021-10-30', '2021-10-30', NULL);
INSERT INTO `task_log` VALUES ('921872a8815342b39eace803c3fca224', '校验RFID信息', 1, '2', '校验通过', '2021-10-30', '2021-10-30', NULL);
INSERT INTO `task_log` VALUES ('ccaf7f810f92490285203b8a29d77742', '校验RFID信息', 0, '2', '绑定记录不存在', '2021-10-30', '2021-10-30', NULL);
INSERT INTO `task_log` VALUES ('cf01ca9ad9a74e96b001b0d229384926', '校验RFID信息', 0, '2', '机床与绑定不匹配', '2021-10-30', '2021-10-30', NULL);

SET FOREIGN_KEY_CHECKS = 1;
