/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : myexam

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 05/03/2019 11:13:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_course
-- ----------------------------
DROP TABLE IF EXISTS `tb_course`;
CREATE TABLE `tb_course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_course
-- ----------------------------
INSERT INTO `tb_course` VALUES (1, 'JAVA面向对象');
INSERT INTO `tb_course` VALUES (2, '程序设计');
INSERT INTO `tb_course` VALUES (3, 'C语言基础');

-- ----------------------------
-- Table structure for tb_paper
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper`;
CREATE TABLE `tb_paper`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '试卷名',
  `course_id` int(11) NOT NULL,
  `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `paper_minute` int(11) NOT NULL COMMENT '考试时长',
  `show_key` int(11) NOT NULL COMMENT '0公布答案',
  `paper_type` int(11) NOT NULL COMMENT '试卷类型,1普通试卷,2随机组卷',
  `to_user` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考生id,隔开',
  `pass_score` int(11) NOT NULL COMMENT '及格分数',
  `config_status` int(11) NOT NULL COMMENT '是否配置',
  `c_uid` int(11) NOT NULL,
  `c_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_paper
-- ----------------------------
INSERT INTO `tb_paper` VALUES (9, '正式考试', 3, '2019-03-05 08:40:00', '2019-03-05 14:00:00', 5, 0, 2, '5', 60, 0, 1, '2019-03-04 19:07:04');

-- ----------------------------
-- Table structure for tb_paper_question
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper_question`;
CREATE TABLE `tb_paper_question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `section_id` int(11) NOT NULL,
  `question_ids` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_paper_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper_result`;
CREATE TABLE `tb_paper_result`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_test_id` int(11) NOT NULL,
  `result_keys` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户提交答案',
  `result_score` int(11) NOT NULL COMMENT '总分',
  `result_minute` int(11) NOT NULL COMMENT '用时',
  `submit_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `question_score` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目分数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `paper_test_id_idx`(`paper_test_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_paper_result
-- ----------------------------
INSERT INTO `tb_paper_result` VALUES (18, 31, '[[\"111\"],[\"139\"],[\"164\"],[\"102\"],[\"78\"],[\"219\",\"220\",\"221\",\"222\"],[\"259\",\"260\",\"261\",\"262\"],[\"297\",\"298\",\"299\",\"300\"],[\"T\"],[\"10\"]]', 30, 4, '2019-03-05 09:02:24', '10,10,10,10,10,10,10,10,10,10');

-- ----------------------------
-- Table structure for tb_paper_section
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper_section`;
CREATE TABLE `tb_paper_section`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_id` int(11) NOT NULL,
  `section_type` int(11) NOT NULL COMMENT '章节类型',
  `question_num` int(11) NOT NULL COMMENT '数量',
  `question_score` int(11) NOT NULL COMMENT '每题分值',
  `qdb_ids` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题库ids',
  `level_scale` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '1:1:1:1:1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_paper_section
-- ----------------------------
INSERT INTO `tb_paper_section` VALUES (22, 9, 1, 5, 10, '1', '1,1,1,1,1');
INSERT INTO `tb_paper_section` VALUES (23, 9, 2, 3, 10, '1', '0,1,1,1,0');
INSERT INTO `tb_paper_section` VALUES (24, 9, 3, 1, 10, '1', '0,0,1,0,0');
INSERT INTO `tb_paper_section` VALUES (25, 9, 4, 1, 10, '1', '0,0,0,0,1');

-- ----------------------------
-- Table structure for tb_paper_test
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper_test`;
CREATE TABLE `tb_paper_test`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paper_user_id` int(11) NOT NULL,
  `question_ids` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目id',
  `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `paper_user_id_idx`(`paper_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_paper_test
-- ----------------------------
INSERT INTO `tb_paper_test` VALUES (31, 14, '55,62,68,25,19,82,91,100,42,140', '2019-03-05 08:57:24');

-- ----------------------------
-- Table structure for tb_paper_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_paper_user`;
CREATE TABLE `tb_paper_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `paper_id` int(11) NOT NULL,
  `status` int(11) NOT NULL COMMENT '1等待开始，2正在进行，0已提交,3未参加',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_paper_idx`(`user_id`, `paper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_paper_user
-- ----------------------------
INSERT INTO `tb_paper_user` VALUES (14, 5, 9, 0);

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `per_title` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `per_url` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES (1, '所有', '/*');
INSERT INTO `tb_permission` VALUES (2, '无', '/');
INSERT INTO `tb_permission` VALUES (3, '题库管理', '/qdb');
INSERT INTO `tb_permission` VALUES (4, '用户管理', '/user');
INSERT INTO `tb_permission` VALUES (5, '考试管理', '/paper');
INSERT INTO `tb_permission` VALUES (6, '成绩管理', '/score');

-- ----------------------------
-- Table structure for tb_permission_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission_menu`;
CREATE TABLE `tb_permission_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `per_id` int(11) NOT NULL,
  `menu_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `menu_url` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission_menu
-- ----------------------------
INSERT INTO `tb_permission_menu` VALUES (3, 3, '科目列表', '/system/qdb/course/list');
INSERT INTO `tb_permission_menu` VALUES (4, 3, '题库列表', '/system/qdb/list');
INSERT INTO `tb_permission_menu` VALUES (5, 3, '试题列表', '/system/qdb/question/list');
INSERT INTO `tb_permission_menu` VALUES (6, 6, '成绩分析', '/system/score/list');
INSERT INTO `tb_permission_menu` VALUES (7, 5, '试卷列表', '/system/paper/list');
INSERT INTO `tb_permission_menu` VALUES (8, 4, '用户列表', '/system/user/list');
INSERT INTO `tb_permission_menu` VALUES (9, 4, '专业列表', '/system/user/student/major/list');
INSERT INTO `tb_permission_menu` VALUES (10, 4, '学生列表', '/system/user/student/list');

-- ----------------------------
-- Table structure for tb_question
-- ----------------------------
DROP TABLE IF EXISTS `tb_question`;
CREATE TABLE `tb_question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `q_content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题干',
  `qdb_id` int(11) NOT NULL COMMENT '题库编号',
  `q_type` int(11) NOT NULL COMMENT '题目类型:单选1,多选2,判断3,填空4',
  `q_level` int(11) NOT NULL COMMENT '等级:1,2,3,4,5',
  `q_status` int(11) NOT NULL COMMENT '状态:0开放(测试练习),1关闭',
  `c_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `u_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `u_uid` int(11) NOT NULL COMMENT '修改人',
  `c_uid` int(11) NOT NULL COMMENT '创建人',
  `opt_key` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案:选项编号,隔开',
  `key_info` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '解析',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_question
-- ----------------------------
INSERT INTO `tb_question` VALUES (5, '下列叙述中正确的是( )', 1, 1, 3, 0, '2019-03-02 17:10:18', '2019-03-02 17:10:18', 3, 3, '20', '无');
INSERT INTO `tb_question` VALUES (6, '设变量x为float型且已赋值，则以下语句中能将x中的数值保留到小数点后两位，并将第三位四舍五入的是( )', 1, 1, 4, 0, '2019-03-02 17:13:05', '2019-03-02 17:13:06', 3, 3, '26', '无');
INSERT INTO `tb_question` VALUES (7, '下列关于二叉树的叙述中，正确的是( )。', 1, 1, 3, 0, '2019-03-02 17:20:07', '2019-03-02 17:20:07', 3, 3, '28', '无');
INSERT INTO `tb_question` VALUES (8, '软件生命周期中的活动不包括( )。', 1, 1, 1, 0, '2019-03-04 20:22:04', '2019-03-04 20:22:04', 1, 3, '31', '无');
INSERT INTO `tb_question` VALUES (9, '若变量均已正确定义并赋值，以下合法的C语言赋值语句是( )。', 1, 1, 2, 0, '2019-03-04 20:20:57', '2019-03-04 20:20:57', 1, 3, '35', '无');
INSERT INTO `tb_question` VALUES (10, '程序调试的任务是( )。', 1, 1, 1, 0, '2019-03-02 17:25:13', '2019-03-02 17:25:13', 3, 3, '42', '无');
INSERT INTO `tb_question` VALUES (11, '下列关于数据库设计的叙述中，正确的是( )。', 1, 1, 1, 0, '2019-03-04 20:22:38', '2019-03-04 20:22:38', 1, 3, '43', '无');
INSERT INTO `tb_question` VALUES (12, '数据库系统的三级模式不包括( )。', 1, 1, 1, 0, '2019-03-02 17:27:21', '2019-03-02 17:27:21', 3, 3, '50', '无');
INSERT INTO `tb_question` VALUES (13, '以下选项中关于C语言常量的叙述错误的是( )。', 1, 1, 2, 0, '2019-03-04 20:23:11', '2019-03-04 20:23:11', 1, 3, '54', '无');
INSERT INTO `tb_question` VALUES (14, '以下选项中叙述错误的是( )。', 1, 1, 1, 0, '2019-03-04 16:14:17', '2019-03-04 16:14:17', 1, 1, '57', '无');
INSERT INTO `tb_question` VALUES (16, '若函数调用时的实参为变量时，以下关于函数形参和实参的叙述中正确的是( )', 1, 1, 5, 0, '2019-03-04 16:50:52', '2019-03-04 16:50:52', 1, 1, '63', '无');
INSERT INTO `tb_question` VALUES (17, '已知字符‘A’的ASCIl代码值是65，字符变量cl的值是‘A’，c2的值是‘D’。则执行语句printf(”%d，%d”，cl，c2—2);的输出结果是( )。', 1, 1, 5, 0, '2019-03-04 16:50:52', '2019-03-04 16:50:52', 1, 1, '70', '无');
INSERT INTO `tb_question` VALUES (18, '以下选项中，当x为大于1的奇数时，值为0的表达式是( )。', 1, 1, 5, 0, '2019-03-04 16:50:52', '2019-03-04 16:50:53', 1, 1, '71', '无');
INSERT INTO `tb_question` VALUES (19, '设fp为指向某二进制文件的指针，且已读到此文件末尾，则函数feof(fp)，的返回值为( )。', 1, 1, 5, 0, '2019-03-04 16:50:53', '2019-03-04 16:50:53', 1, 1, '77', '无');
INSERT INTO `tb_question` VALUES (20, '以下能正确定义字符串的语句是( )。', 1, 1, 5, 0, '2019-03-04 16:50:53', '2019-03-04 16:50:53', 1, 1, '80', '无');
INSERT INTO `tb_question` VALUES (21, '以下选项中，能用作用户标识符的是( )。', 1, 1, 4, 0, '2019-03-04 16:56:37', '2019-03-04 16:56:37', 1, 1, '83', '无');
INSERT INTO `tb_question` VALUES (22, '若有定义语句：int X=10;，则表达式x一=x+x的值为( )。', 1, 1, 4, 0, '2019-03-04 16:56:37', '2019-03-04 16:56:37', 1, 1, '89', '无');
INSERT INTO `tb_question` VALUES (23, '以下叙述中错误的是( )。', 1, 1, 4, 0, '2019-03-04 16:56:37', '2019-03-04 16:56:37', 1, 1, '92', '无');
INSERT INTO `tb_question` VALUES (24, '设有定义：char*C;以下选项中能够使C正确指向一个字符串的是( )。', 1, 1, 4, 0, '2019-03-04 16:56:37', '2019-03-04 16:56:37', 1, 1, '95', '无');
INSERT INTO `tb_question` VALUES (25, '若有定义语句：char S[10]=”1234567\\0\\0”;则strlen(s)的值是( )。', 1, 1, 4, 0, '2019-03-04 16:57:39', '2019-03-04 16:57:39', 1, 1, '99', '无');
INSERT INTO `tb_question` VALUES (26, '以下选项中不能作为c语言合法常量的是( )。.', 1, 1, 2, 0, '2019-03-04 17:09:48', '2019-03-04 17:09:48', 1, 1, '104', '无');
INSERT INTO `tb_question` VALUES (27, '设a,b,c为int型变量，且a=3, b= 4, c=5，下面表达式值为0的是( )。', 1, 2, 2, 0, '2019-03-04 17:09:48', '2019-03-04 17:09:48', 1, 1, '108,110', '无');
INSERT INTO `tb_question` VALUES (28, '在if语句的三种形式中，如果要想在满足条件时执行一组(多个)语句，则必须把这一组语句用{}括起来组成一个复合语句。', 1, 3, 1, 0, '2019-03-04 17:09:48', '2019-03-04 17:09:48', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (29, '若有定义int a[]={2,4,6,8,10},p=a;a的值是数组首地址，则*(p+1)的值是（____）', 1, 4, 2, 0, '2019-03-04 18:46:22', '2019-03-04 18:46:22', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (30, '任何表达式语句都是表达式加分号组成的。', 1, 3, 1, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (31, 'do-while循环的while后的分号可以省略。 ', 1, 3, 1, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (32, 'case语句后如没有break，顺序向下执行。', 1, 3, 1, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (33, '增1减1运算符的前缀运算和后缀运算的表达式值是相同的。', 1, 3, 1, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (34, '函数的实参可以是常量，变量或表达式。', 1, 3, 1, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (35, '如果函数定义出现在函数调用之前，可以不必加函数原型声明。', 1, 3, 2, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (36, '.C 语言程序中可以有多个函数 , 但只能有一个主函数。', 1, 3, 2, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (37, '函数返回值的类型是由在定义函数时所指定的函数类型。', 1, 3, 2, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (38, '局部变量如果没有指定初值，则其初值不确定。', 1, 3, 2, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (39, '.若有定义int a[]={2,4,6,8,10},p=a;a的值是数组首地址，则*(p+1)的值是4。', 1, 3, 2, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (40, '指针数组的每个元素都是一个指针变量。', 1, 3, 3, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (41, '关于C语言指针的运算：指针只有加减操作，没有乘除操作。指针可以加常数、减常数；相同类型的指针可以相加、相减。 ', 1, 3, 3, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (42, '假设有定义如下： int array[10]; 则该语句定义了一个数组array。其中array的类型是整型指针（即： int *）。', 1, 3, 3, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (43, 'C 语言具有简洁明了的特点', 1, 3, 3, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (44, '预处理命令的前面必须加一个“#”号。', 1, 3, 3, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (45, '标准格式输入函数scanf()可以从键盘上接收不同数据类型的数据项。', 1, 3, 4, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (46, '在if语句的三种形式中，如果要想在满足条件时执行一组(多个)语句，则必须把这一组语句用{}括起来组成一个复合语句。', 1, 3, 4, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (47, 'continue 不是结束本次循环，而是终止整个循环的执行。', 1, 3, 4, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (48, '在对数组全部元素赋初值时，不可以省略行数，但能省略列数', 1, 3, 4, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (49, '函数的实参传递到形参有两种方式值传递和地址传递', 1, 3, 4, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (50, '直接访问就是直接利用变量的地址进行存取直接访问', 1, 3, 5, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (51, '共用体变量可以作结构体的成员，结构体变量也可以作共用体的成员。', 1, 3, 5, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (52, '文件指针和位置指针都是随着文件的读写操作在不断改变。', 1, 3, 5, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (53, 'C 语言标准格式输入函数 scanf() 的参数表中要使用变量的地址值。', 1, 3, 5, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'T', '无');
INSERT INTO `tb_question` VALUES (54, '浮点型常量的指数表示中 ,e 是可以省略的。', 1, 3, 5, 0, '2019-03-04 17:17:54', '2019-03-04 17:17:54', 1, 1, 'F', '无');
INSERT INTO `tb_question` VALUES (55, '设文件指针fp已定义，执行语句fp=fopen(”61e”，”W”);后，以下针对文本文件file操作叙述的选项中正确的是( )。', 1, 1, 1, 0, '2019-03-04 17:39:03', '2019-03-04 17:39:04', 1, 1, '111', '无');
INSERT INTO `tb_question` VALUES (56, '以下选项中叙述错误的是( )。', 1, 1, 2, 0, '2019-03-04 17:39:04', '2019-03-04 17:39:04', 1, 1, '117', '无');
INSERT INTO `tb_question` VALUES (57, '以下选项中正确的语句组是( )。', 1, 1, 5, 0, '2019-03-04 17:39:04', '2019-03-04 17:39:04', 1, 1, '120', '无');
INSERT INTO `tb_question` VALUES (58, '若函数调用时的实参为变量时，以下关于函数形参和实参的叙述中正确的是( )。', 1, 1, 3, 0, '2019-03-04 17:39:04', '2019-03-04 17:39:05', 1, 1, '123', '无');
INSERT INTO `tb_question` VALUES (59, '一个栈的初始状态为空。现将元素1、2、3、4、5、A、B、C、D、E依次入栈，然后再依次出栈，则元素出栈的顺序是(　　)。', 1, 1, 1, 0, '2019-03-04 17:39:05', '2019-03-04 17:39:05', 1, 1, '128', '无');
INSERT INTO `tb_question` VALUES (60, '下列叙述中正确的是(　　)。', 1, 1, 2, 0, '2019-03-04 17:39:05', '2019-03-04 17:39:05', 1, 1, '134', '无');
INSERT INTO `tb_question` VALUES (61, '在长度为n的有序线性表中进行二分查找，最坏情况下需要比较的次数是(　　)。', 1, 1, 2, 0, '2019-03-04 17:39:05', '2019-03-04 17:39:05', 1, 1, '138', '无');
INSERT INTO `tb_question` VALUES (62, '下列叙述中正确的是(　　)。', 1, 1, 2, 0, '2019-03-04 17:39:05', '2019-03-04 17:39:05', 1, 1, '139', '无');
INSERT INTO `tb_question` VALUES (63, '数据流图中带有箭头的线段表示的是(　　)。', 1, 1, 2, 0, '2019-03-04 17:39:05', '2019-03-04 17:39:06', 1, 1, '146', '无');
INSERT INTO `tb_question` VALUES (64, '在软件开发中，需求分析阶段可以使用的工具是(　　)。', 1, 1, 2, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:06', 1, 1, '148', '无');
INSERT INTO `tb_question` VALUES (65, '在面向对象方法中，不属于“对象”基本特点的是(　　)。', 1, 1, 3, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:06', 1, 1, '151', '无');
INSERT INTO `tb_question` VALUES (66, '一间宿舍可住多个学生，则实体宿舍和学生之间的联系是(　　)。', 1, 1, 3, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:06', 1, 1, '155', '无');
INSERT INTO `tb_question` VALUES (67, '在数据管理技术发展的三个阶段中，数据共享最好的是(　　)。', 1, 1, 3, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:06', 1, 1, '161', '无');
INSERT INTO `tb_question` VALUES (68, '下列叙述中错误的是( )。', 1, 1, 3, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:06', 1, 1, '164', '无');
INSERT INTO `tb_question` VALUES (69, '以下关于逻辑运算符两侧运算对象的叙述中正确的是( )。', 1, 1, 3, 0, '2019-03-04 17:39:06', '2019-03-04 17:39:07', 1, 1, '167', '无');
INSERT INTO `tb_question` VALUES (70, '若有定义：double a=22;inti=0，k=18;，则不符合c语言规定的赋值语句是( )。', 1, 1, 4, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:07', 1, 1, '171', '无');
INSERT INTO `tb_question` VALUES (71, '以下叙述中错误的是( )。', 1, 1, 4, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:07', 1, 1, '175', '无');
INSERT INTO `tb_question` VALUES (72, '以下选项中不合法的标识符是( )。', 1, 1, 4, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:07', 1, 1, '179', '无');
INSERT INTO `tb_question` VALUES (73, '以下叙述中正确的是( )。', 1, 1, 4, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:07', 1, 1, '183', '无');
INSERT INTO `tb_question` VALUES (74, '在一个C源程序文件中所定义的全局变量，其作用域为( )。', 1, 1, 4, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:07', 1, 1, '187', '无');
INSERT INTO `tb_question` VALUES (75, '以下叙述中错误的是( )。', 1, 1, 5, 0, '2019-03-04 17:39:07', '2019-03-04 17:39:08', 1, 1, '191', '无');
INSERT INTO `tb_question` VALUES (76, '以下不能将s所指字符串正确复制到t所指存储空间的是( )。', 1, 1, 5, 0, '2019-03-04 17:39:08', '2019-03-04 17:39:08', 1, 1, '195', '无');
INSERT INTO `tb_question` VALUES (77, '下列叙述中正确的是(　　)。', 1, 1, 5, 0, '2019-03-04 17:39:08', '2019-03-04 17:39:08', 1, 1, '202', '无');
INSERT INTO `tb_question` VALUES (78, '支持子程序调用的数据结构是(　　)。', 1, 1, 5, 0, '2019-03-04 17:39:08', '2019-03-04 17:39:08', 1, 1, '203', '无');
INSERT INTO `tb_question` VALUES (79, '某二叉树有5个度为2的结点，则该二叉树中的叶子结点数是(　　)。', 1, 1, 5, 0, '2019-03-04 17:39:08', '2019-03-04 17:39:08', 1, 1, '209', '无');
INSERT INTO `tb_question` VALUES (80, '下列排序方法中，最坏情况下比较次数最少的是(　　)。', 1, 1, 5, 0, '2019-03-04 17:39:08', '2019-03-04 17:39:08', 1, 1, '214', '无');
INSERT INTO `tb_question` VALUES (81, '全党同志一定要永远与人民____、____、____，永远把人民对美好生活的向往作为奋斗目标，以永不懈怠的精神状态和一往无前的奋斗姿态，继续朝着实现中华民族伟大复兴的宏伟目标奋勇前进。', 1, 2, 1, 0, '2019-03-04 18:08:25', '2019-03-04 18:08:25', 1, 1, '215,216,218', '无');
INSERT INTO `tb_question` VALUES (82, '过去五年，开放型经济新体制逐步健全，____、____、____稳居世界前列。', 1, 2, 2, 0, '2019-03-04 18:08:25', '2019-03-04 18:08:25', 1, 1, '219,220,221', '无');
INSERT INTO `tb_question` VALUES (83, '科学立法、严格执法、公正司法、全民守法深入推进，____、____、____建设相互促进，中国特色社会主义法治体系日益完善，全社会法治观念明显增强。', 1, 2, 5, 0, '2019-03-04 18:08:25', '2019-03-04 18:08:25', 1, 1, '224,225,226', '无');
INSERT INTO `tb_question` VALUES (84, '引导应对气候变化国际合作，成为全球生态文明建设的重要____、____、____。', 1, 2, 3, 0, '2019-03-04 18:08:25', '2019-03-04 18:08:26', 1, 1, '227,228,229', '无');
INSERT INTO `tb_question` VALUES (85, '出台中央八项规定，严厉整治____、____、____和____，坚决反对特权。', 1, 2, 1, 0, '2019-03-04 18:08:26', '2019-03-04 18:08:26', 1, 1, '231,232,233,234', '无');
INSERT INTO `tb_question` VALUES (86, '五年来，我们勇于面对党面临的重大风险考验和党内存在的突出问题，以顽强意志品质正风肃纪、反腐惩恶，消除了党和国家内部存在的严重隐患，党内政治生活气象更新，党内政治生态明显好转，党的_______显著增强。', 1, 2, 2, 0, '2019-03-04 18:08:26', '2019-03-04 18:08:26', 1, 1, '236,238,239', '无');
INSERT INTO `tb_question` VALUES (87, '坚持______的要求，开展党的群众路线教育实践活动和“三严三实”专题教育，推进“两学一做”学习教育常态化制度化，全党理想信念更加坚定、党性更加坚强。', 1, 2, 2, 0, '2019-03-04 18:08:26', '2019-03-04 18:08:26', 1, 1, '240,241,242,243', '无');
INSERT INTO `tb_question` VALUES (88, '这个新时代，是____。', 1, 2, 2, 0, '2019-03-04 18:08:26', '2019-03-04 18:08:27', 1, 1, '244,245,246,247,248', '无');
INSERT INTO `tb_question` VALUES (89, '全党要更加自觉地增强____、____、____、____，既不走封闭僵化的老路，也不走改旗易帜的邪路，保持政治定力，坚持实干兴邦，始终坚持和发展中国特色社会主义。', 1, 2, 2, 0, '2019-03-04 18:08:27', '2019-03-04 18:08:27', 1, 1, '249,250,251,252', '无');
INSERT INTO `tb_question` VALUES (90, '新时代中国特色社会主义思想，是_____，必须长期坚持并不断发展。', 1, 2, 2, 0, '2019-03-04 18:08:27', '2019-03-04 18:08:27', 1, 1, '254,255,256,257,258', '无');
INSERT INTO `tb_question` VALUES (91, '新时代坚持和发展中国特色社会主义的基本方略是____。', 1, 2, 3, 0, '2019-03-04 18:08:27', '2019-03-04 18:08:28', 1, 1, '259,260,261,262,263', '无');
INSERT INTO `tb_question` VALUES (92, '党政军民学，东西南北中，党是领导一切的。必须增强____，自觉维护党中央权威和集中统一领导，自觉在思想上政治上行动上同党中央保持高度一致。', 1, 2, 3, 0, '2019-03-04 18:08:28', '2019-03-04 18:08:28', 1, 1, '264,265,266,267', '无');
INSERT INTO `tb_question` VALUES (93, '建设一支____、____、____的人民军队，是实现“两个一百年”奋斗目标、实现中华民族伟大复兴的战略支撑。', 1, 2, 3, 0, '2019-03-04 18:08:28', '2019-03-04 18:08:28', 1, 1, '269,271,272', '无');
INSERT INTO `tb_question` VALUES (94, '实施乡村振兴战略。______问题是关系国计民生的根本性问题，必须始终把解决好“三农”问题作为全党工作重中之重。', 1, 2, 3, 0, '2019-03-04 18:08:28', '2019-03-04 18:08:28', 1, 1, '273,274,275', '无');
INSERT INTO `tb_question` VALUES (95, '巩固和发展爱国统一战线。坚持______，支持民主党派按照中国特色社会主义参政党要求更好履行职能。', 1, 2, 3, 0, '2019-03-04 18:08:28', '2019-03-04 18:08:28', 1, 1, '277,278,279,280', '无');
INSERT INTO `tb_question` VALUES (96, '提高就业质量和人民收入水平，鼓励勤劳守法致富，就要____。', 1, 2, 4, 0, '2019-03-04 18:08:28', '2019-03-04 18:08:29', 1, 1, '281,282,283,284', '无');
INSERT INTO `tb_question` VALUES (97, '加强社会保障体系建设。坚持房子是用来住的、不是用来炒的定位，加快建立______的住房制度，让全体人民住有所居。', 1, 2, 4, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '285,286,287', '无');
INSERT INTO `tb_question` VALUES (98, '加强社会治理制度建设，完善党委领导、政府负责、社会协同、公众参与、法治保障的社会治理体制，提高社会治理________水平。', 1, 2, 4, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '289,290,291,292', '无');
INSERT INTO `tb_question` VALUES (99, '必须坚持____、____、____为主的方针，形成节约资源和保护环境的空间格局、产业结构、生产方式、生活方式，还自然以宁静、和谐、美丽。', 1, 2, 4, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '294,295,296', '无');
INSERT INTO `tb_question` VALUES (100, '构建市场导向的绿色技术创新体系，发展绿色金融，壮大____、____、____。', 1, 2, 4, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '297,298,300', '无');
INSERT INTO `tb_question` VALUES (101, '倡导简约适度、绿色低碳的生活方式，反对奢侈浪费和不合理消费，开展创建节约型机关、_____、_____、_____、_____等行动。', 1, 2, 5, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '301,302,303,305', '无');
INSERT INTO `tb_question` VALUES (102, '提高污染排放标准，强化排污者责任，健全_______、_______、______等制度。', 1, 2, 5, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:29', 1, 1, '306,308,309', '无');
INSERT INTO `tb_question` VALUES (103, '完成_____、_____、_____三条控制线划定工作。', 1, 2, 5, 0, '2019-03-04 18:08:29', '2019-03-04 18:08:30', 1, 1, '310,311,312', '无');
INSERT INTO `tb_question` VALUES (104, '保持香港、澳门长期繁荣稳定，必须全面准确贯彻____的方针。', 1, 2, 5, 0, '2019-03-04 18:08:30', '2019-03-04 18:08:30', 1, 1, '314,315,316,317', '无');
INSERT INTO `tb_question` VALUES (105, '要支持香港、澳门融入国家发展大局，以____、____、____等为重点，全面推进内地同香港、澳门互利合作。', 1, 2, 5, 0, '2019-03-04 18:08:30', '2019-03-04 18:08:30', 1, 1, '318,319,321', '无');
INSERT INTO `tb_question` VALUES (106, '中国将高举___、___、___、___的旗帜，恪守维护世界和平、促进共同发展的外交政策宗旨。', 1, 2, 5, 0, '2019-03-04 18:08:30', '2019-03-04 18:08:31', 1, 1, '322,323,324,325', '无');
INSERT INTO `tb_question` VALUES (107, '坚定不移在和平共处五项原则基础上发展同各国的友好合作，推动建设____、____、____的新型国际关系。', 1, 2, 1, 0, '2019-03-04 18:08:31', '2019-03-04 18:08:31', 1, 1, '327,328,330', '无');
INSERT INTO `tb_question` VALUES (108, '世界正处于大发展大变革大调整时期，和平与发展仍然是时代主题。____、____、____、____深入发展。', 1, 2, 1, 0, '2019-03-04 18:08:31', '2019-03-04 18:08:31', 1, 1, '331,332,333,334', '无');
INSERT INTO `tb_question` VALUES (109, '积极促进“一带一路”国际合作，努力实现____、____、____、____、____，打造国际合作新平台，增添共同发展新动力。', 1, 2, 1, 0, '2019-03-04 18:08:31', '2019-03-04 18:08:32', 1, 1, '336,337,338,340,341', '无');
INSERT INTO `tb_question` VALUES (110, '要深刻认识党面临的____的长期性和复杂性。', 1, 2, 1, 0, '2019-03-04 18:08:32', '2019-03-04 18:08:32', 1, 1, '342,343,344,346', '无');
INSERT INTO `tb_question` VALUES (111, '中国共产党人的初心和使命是（____）', 1, 4, 1, 0, '2019-03-04 18:44:40', '2019-03-04 18:44:40', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (112, '中国特色社会主义进入新时代，我国社会主要矛盾已经转化为（____）', 1, 4, 2, 0, '2019-03-04 18:46:31', '2019-03-04 18:46:31', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (113, '新时代中国共产党的历史使命是（____）', 1, 4, 5, 0, '2019-03-04 18:51:19', '2019-03-04 18:51:19', 1, 1, '实现中华民族伟大复兴是近代以来中华民族最伟大的梦想。', '无');
INSERT INTO `tb_question` VALUES (114, '新时代中国特色社会主义思想明确坚持和发展中国特色社会主义，总任务是（____）在全面建成小康社会的基础上，分两步走在本世纪中叶建成（____）的社会主义现代化强国。', 1, 4, 3, 0, '2019-03-04 18:48:22', '2019-03-04 18:48:22', 1, 1, '实现社会主义现代化和中华民族伟大复兴,富强民主文明和谐美丽', '无');
INSERT INTO `tb_question` VALUES (115, '数学课本的宽大约是（____）厘米。', 1, 4, 1, 0, '2019-03-04 18:44:55', '2019-03-04 18:44:55', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (116, '100条1厘米长的线段一条接一条，接成一条长线段，这条长线段是（____）米。', 1, 4, 2, 0, '2019-03-04 18:46:37', '2019-03-04 18:46:37', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (117, '小明有两件颜色不同的上衣和两条颜色不同的裤子，他可以有（____）种不同的穿法。 ', 1, 4, 2, 0, '2019-03-04 20:23:41', '2019-03-04 20:23:41', 1, 1, '4', '无');
INSERT INTO `tb_question` VALUES (118, '三个小朋友，进行乒乓球比赛，每两人进行一次，一共要进行（____）次比赛。 ', 1, 4, 2, 0, '2019-03-04 18:46:44', '2019-03-04 18:46:44', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (119, '小明、小红、小丽三人玩拍球比赛，三人拍球的次数分别是36下、35下、33下，小明拍的次数最多，小丽拍了33下，小红拍了（____）下。 ', 1, 4, 2, 0, '2019-03-04 18:46:53', '2019-03-04 18:46:53', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (120, '2.5时=（____）分，2元4分=（____）元。', 1, 4, 2, 0, '2019-03-04 18:47:28', '2019-03-04 18:47:28', 1, 1, '答案,答案', '无');
INSERT INTO `tb_question` VALUES (121, '把一个棱长4厘米的大正方体切成棱长1厘米的小正方体，可以切成（____）个小正方体。 ', 1, 4, 3, 0, '2019-03-04 18:48:37', '2019-03-04 18:48:37', 1, 1, '16', '无');
INSERT INTO `tb_question` VALUES (122, '一间教室长12米，宽8米，画在比例尺是1︰400的平面图上，长应画（____）厘米，宽应画（____）厘米。', 1, 4, 3, 0, '2019-03-04 18:49:09', '2019-03-04 18:49:09', 1, 1, '3,2', '无');
INSERT INTO `tb_question` VALUES (123, '五年一班在上学期期末检测时，有2名学生不及格，及格率是95﹪，五年一班共有学生（____）名。', 1, 4, 3, 0, '2019-03-04 18:48:48', '2019-03-04 18:48:48', 1, 1, '40', '无');
INSERT INTO `tb_question` VALUES (124, '将一个周长是16分米的平行四边形框架拉成一个长方形，这个长方形的周长是（____）分米。', 1, 4, 3, 0, '2019-03-04 18:49:21', '2019-03-04 18:49:21', 1, 1, '16', '无');
INSERT INTO `tb_question` VALUES (125, '在分数单位是1/9的分数中最大的真分数是（____），最小的假分数是（____）。', 1, 4, 3, 0, '2019-03-04 18:49:44', '2019-03-04 18:49:44', 1, 1, '1,2', '无');
INSERT INTO `tb_question` VALUES (126, '一个直角三角形的两条直角边分别是4厘米和3厘米，这个直角三角形的面积是（____）平方厘米。', 1, 4, 4, 0, '2019-03-04 18:50:27', '2019-03-04 18:50:27', 1, 1, '12', '无');
INSERT INTO `tb_question` VALUES (127, '15、30和60三个数的最小公倍数是（____），最大公因数是（____）。', 1, 4, 4, 0, '2019-03-04 18:50:41', '2019-03-04 18:50:41', 1, 1, '10,20', '无');
INSERT INTO `tb_question` VALUES (128, '某家电商场“五·一”期间开展大酬宾活动，全场家电按80%销售，原价150元的电饭锅，现在售价是（____）元。', 1, 4, 4, 0, '2019-03-04 18:50:51', '2019-03-04 18:50:51', 1, 1, '120', '无');
INSERT INTO `tb_question` VALUES (129, '圆规两脚间距离为1厘米，画出的圆的周长是（____）厘米。', 1, 4, 4, 0, '2019-03-04 18:51:00', '2019-03-04 18:51:00', 1, 1, '6.28', '无');
INSERT INTO `tb_question` VALUES (130, '在3：a中，如果比的前项扩大3倍，要使比值不变，后项应加上（____）。', 1, 4, 4, 0, '2019-03-04 18:51:09', '2019-03-04 18:51:09', 1, 1, '2a', '无');
INSERT INTO `tb_question` VALUES (131, '甲数是30，乙数是甲数的3/5，乙数是（____）', 1, 4, 5, 0, '2019-03-04 18:51:29', '2019-03-04 18:51:29', 1, 1, '18', '无');
INSERT INTO `tb_question` VALUES (132, '乙数是18，正好是甲数的3/5，甲数是（____）', 1, 4, 5, 0, '2019-03-04 18:51:38', '2019-03-04 18:51:38', 1, 1, '30', '无');
INSERT INTO `tb_question` VALUES (133, '一个数的4/7是28，这个数是（____）。', 1, 4, 5, 0, '2019-03-04 18:51:47', '2019-03-04 18:51:47', 1, 1, '49', '无');
INSERT INTO `tb_question` VALUES (134, '2/3÷16表示', 1, 2, 5, 0, '2019-03-04 18:30:23', '2019-03-04 18:30:23', 1, 1, 's', '无');
INSERT INTO `tb_question` VALUES (135, '100克水里加20克糖，糖水的含糖率约是（____）％。', 1, 4, 1, 0, '2019-03-04 18:45:07', '2019-03-04 18:45:07', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (136, '一个半圆的直径是6分米，它的周长是（____）分米，面积是（____）平方分米。', 1, 4, 1, 0, '2019-03-04 18:45:35', '2019-03-04 18:45:35', 1, 1, '答案,答案', '无');
INSERT INTO `tb_question` VALUES (137, 'a x 1/5 = b x 2/7，那么a：b=（____）：（____）。', 1, 4, 1, 0, '2019-03-04 18:45:52', '2019-03-04 18:45:52', 1, 1, '答案,答案', '无');
INSERT INTO `tb_question` VALUES (138, '紧靠一道围墙边，用18米长的竹篱笆围出一块长方形（边长为整数）的菜地，这块菜地的面积最大是（____）平方米。', 1, 4, 1, 0, '2019-03-04 18:46:01', '2019-03-04 18:46:01', 1, 1, '答案', '无');
INSERT INTO `tb_question` VALUES (139, '三个质数的倒数和是311/1001，则这三个质数分别为（____），（____），（____）。', 1, 4, 5, 0, '2019-03-04 18:52:04', '2019-03-04 18:52:04', 1, 1, '7,11,13', '无');
INSERT INTO `tb_question` VALUES (140, '一个长方形长宽之比是4：3，面积是432平方厘米，它的周长是（____）厘米。', 1, 4, 5, 0, '2019-03-04 18:52:18', '2019-03-04 18:52:18', 1, 1, '20', '无');
INSERT INTO `tb_question` VALUES (141, '一辆快车和一辆慢车同时分别从甲、乙两地相对开出，经l2小时后相遇，快车又行驶了8小时到达乙地，那么相遇后慢车还要行驶（____）小时才能到达甲地。', 1, 4, 3, 0, '2019-03-04 18:50:11', '2019-03-04 18:50:11', 1, 1, '5', '无');

-- ----------------------------
-- Table structure for tb_question_db
-- ----------------------------
DROP TABLE IF EXISTS `tb_question_db`;
CREATE TABLE `tb_question_db`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `qdb_name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题库名',
  `qdb_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题库说明',
  `course_id` int(11) NOT NULL COMMENT '课程编号',
  `qdb_status` int(11) NOT NULL COMMENT '题库状态，0开启，1关闭',
  `c_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `u_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `c_uid` int(11) NOT NULL COMMENT '创建人',
  `u_uid` int(11) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_question_db
-- ----------------------------
INSERT INTO `tb_question_db` VALUES (1, 'C语言基础题库', '基础题练习', 3, 0, '2019-02-13 21:08:19', '2019-02-13 21:09:22', 1, 3);
INSERT INTO `tb_question_db` VALUES (3, 'JAVA基础题库', '基础题练习', 1, 0, '2019-02-13 21:09:10', '2019-02-13 21:09:10', 3, 3);

-- ----------------------------
-- Table structure for tb_question_opt
-- ----------------------------
DROP TABLE IF EXISTS `tb_question_opt`;
CREATE TABLE `tb_question_opt`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `option_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选项',
  `q_id` int(11) NOT NULL COMMENT '题目编号',
  `order_num` int(11) NOT NULL COMMENT '序号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 347 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_question_opt
-- ----------------------------
INSERT INTO `tb_question_opt` VALUES (5, '多态', 2, 1);
INSERT INTO `tb_question_opt` VALUES (6, '泛型', 2, 2);
INSERT INTO `tb_question_opt` VALUES (7, '封装', 2, 3);
INSERT INTO `tb_question_opt` VALUES (8, '继承', 2, 4);
INSERT INTO `tb_question_opt` VALUES (17, '模板', 2, 5);
INSERT INTO `tb_question_opt` VALUES (18, '算法就是程序', 5, 1);
INSERT INTO `tb_question_opt` VALUES (19, '设计算法时只需要考虑数据结构的设计', 5, 2);
INSERT INTO `tb_question_opt` VALUES (20, 'java是面向对象语言', 5, 3);
INSERT INTO `tb_question_opt` VALUES (22, '设计算法时只需要考虑结果的可靠性', 5, 4);
INSERT INTO `tb_question_opt` VALUES (23, 'x=x*100+0.5/100.0;', 6, 1);
INSERT INTO `tb_question_opt` VALUES (24, 'x=(x*100+0.5)/100.0;', 6, 2);
INSERT INTO `tb_question_opt` VALUES (25, 'x=(int)(x*100+0.5)/100.0;', 6, 3);
INSERT INTO `tb_question_opt` VALUES (26, 'x=(x/100+0.5)*100.0;', 6, 4);
INSERT INTO `tb_question_opt` VALUES (27, '叶子结点总是比度为2的结点少一个', 7, 1);
INSERT INTO `tb_question_opt` VALUES (28, '叶子结点总是比度为2的结点多一个', 7, 2);
INSERT INTO `tb_question_opt` VALUES (29, '叶子结点数是度为2的结点数的两倍', 7, 3);
INSERT INTO `tb_question_opt` VALUES (31, '市场调研', 8, 1);
INSERT INTO `tb_question_opt` VALUES (32, '需求分析', 8, 2);
INSERT INTO `tb_question_opt` VALUES (33, '软件测试', 8, 3);
INSERT INTO `tb_question_opt` VALUES (34, '软件维护', 8, 4);
INSERT INTO `tb_question_opt` VALUES (35, 'X=5*Y;', 9, 1);
INSERT INTO `tb_question_opt` VALUES (36, 'X=n%2.5;', 9, 2);
INSERT INTO `tb_question_opt` VALUES (37, 'X+n=i;', 9, 3);
INSERT INTO `tb_question_opt` VALUES (38, 'x=5=4+1；', 9, 4);
INSERT INTO `tb_question_opt` VALUES (39, '设计测试用例', 10, 1);
INSERT INTO `tb_question_opt` VALUES (40, '验证程序的正确性', 10, 2);
INSERT INTO `tb_question_opt` VALUES (41, '发现程序中的错误', 10, 3);
INSERT INTO `tb_question_opt` VALUES (42, '诊断和改正程序中的错误', 10, 4);
INSERT INTO `tb_question_opt` VALUES (43, '在需求分析阶段建立数据字典', 11, 1);
INSERT INTO `tb_question_opt` VALUES (44, '在概念设计阶段建立数据字典\r\n', 11, 2);
INSERT INTO `tb_question_opt` VALUES (45, '在逻辑设计阶段建立数据字典', 11, 3);
INSERT INTO `tb_question_opt` VALUES (46, '在物理设计阶段建立数据字典', 11, 4);
INSERT INTO `tb_question_opt` VALUES (47, '概念模式', 12, 1);
INSERT INTO `tb_question_opt` VALUES (48, '内模式', 12, 2);
INSERT INTO `tb_question_opt` VALUES (49, '外模式', 12, 3);
INSERT INTO `tb_question_opt` VALUES (50, '数据模式', 12, 4);
INSERT INTO `tb_question_opt` VALUES (51, '经常被使用的变量可以定义成常量', 13, 1);
INSERT INTO `tb_question_opt` VALUES (52, '常量分为整型常量、实型常量、字符常量和字符串常量', 13, 2);
INSERT INTO `tb_question_opt` VALUES (53, '常量可分为数值型常量和非数值型常量.', 13, 3);
INSERT INTO `tb_question_opt` VALUES (54, '所谓常量，是指在程序运行过程中，其值不能被改变的量', 13, 4);
INSERT INTO `tb_question_opt` VALUES (55, 'c程序函数中定义的自动变量，系统不自动赋确定的初值', 14, 1);
INSERT INTO `tb_question_opt` VALUES (56, '在C程序的同一函数中，各复合语句内可以定义变量，其作用域仅限本复合语句内', 14, 2);
INSERT INTO `tb_question_opt` VALUES (57, 'c程序函数中定义的赋有初值的静态变量，每调用一次函数，赋一次初值', 14, 3);
INSERT INTO `tb_question_opt` VALUES (58, 'c程序函数的形参不可以说明为static型变量', 14, 4);
INSERT INTO `tb_question_opt` VALUES (59, '函数的形参和实参分别占用不同的存储单元', 15, 1);
INSERT INTO `tb_question_opt` VALUES (60, '形参只是形式上的存在，不占用具体存储单元', 15, 2);
INSERT INTO `tb_question_opt` VALUES (61, '同名的实参和形参占同一存储单元', 15, 3);
INSERT INTO `tb_question_opt` VALUES (62, '函数的实参和其对应的形参共占同一存储单元', 15, 4);
INSERT INTO `tb_question_opt` VALUES (63, '函数的形参和实参分别占用不同的存储单元', 16, 1);
INSERT INTO `tb_question_opt` VALUES (64, '形参只是形式上的存在，不占用具体存储单元', 16, 2);
INSERT INTO `tb_question_opt` VALUES (65, '同名的实参和形参占同一存储单元', 16, 3);
INSERT INTO `tb_question_opt` VALUES (66, '函数的实参和其对应的形参共占同一存储单元', 16, 4);
INSERT INTO `tb_question_opt` VALUES (67, '65，68', 17, 1);
INSERT INTO `tb_question_opt` VALUES (68, 'A，68', 17, 2);
INSERT INTO `tb_question_opt` VALUES (69, 'A，B', 17, 3);
INSERT INTO `tb_question_opt` VALUES (70, '65，66', 17, 4);
INSERT INTO `tb_question_opt` VALUES (71, 'x%2==O', 18, 1);
INSERT INTO `tb_question_opt` VALUES (72, 'x/2', 18, 2);
INSERT INTO `tb_question_opt` VALUES (73, 'x%21=O', 18, 3);
INSERT INTO `tb_question_opt` VALUES (74, 'x%2==1', 18, 4);
INSERT INTO `tb_question_opt` VALUES (75, '0', 19, 1);
INSERT INTO `tb_question_opt` VALUES (76, '\\0', 19, 2);
INSERT INTO `tb_question_opt` VALUES (77, '非0值', 19, 3);
INSERT INTO `tb_question_opt` VALUES (78, 'NULL', 19, 4);
INSERT INTO `tb_question_opt` VALUES (79, 'char str=”\\x43”;', 20, 1);
INSERT INTO `tb_question_opt` VALUES (80, 'char str[]=”、0”;', 20, 2);
INSERT INTO `tb_question_opt` VALUES (81, 'char str=”;', 20, 3);
INSERT INTO `tb_question_opt` VALUES (82, 'char str[]={’\\064’};', 20, 4);
INSERT INTO `tb_question_opt` VALUES (83, '一0一', 21, 1);
INSERT INTO `tb_question_opt` VALUES (84, '8-;8', 21, 2);
INSERT INTO `tb_question_opt` VALUES (85, 'void', 21, 3);
INSERT INTO `tb_question_opt` VALUES (86, 'unsigned', 21, 4);
INSERT INTO `tb_question_opt` VALUES (87, 'O', 22, 1);
INSERT INTO `tb_question_opt` VALUES (88, '一20', 22, 2);
INSERT INTO `tb_question_opt` VALUES (89, '一10', 22, 3);
INSERT INTO `tb_question_opt` VALUES (90, '10', 22, 4);
INSERT INTO `tb_question_opt` VALUES (91, 'gets函数用于从终端读人字符串', 23, 1);
INSERT INTO `tb_question_opt` VALUES (92, 'getehar函数用于从磁盘文件读人字符', 23, 2);
INSERT INTO `tb_question_opt` VALUES (93, 'fputs函数用于把字符串输出到文件', 23, 3);
INSERT INTO `tb_question_opt` VALUES (94, 'fwrite函数用于以二进制形式输出数据到文件', 23, 4);
INSERT INTO `tb_question_opt` VALUES (95, 'char str( )=”strin9”;C=str;', 24, 1);
INSERT INTO `tb_question_opt` VALUES (96, 'scanf(”%s”，C);', 24, 2);
INSERT INTO `tb_question_opt` VALUES (97, 'c=getchar( );', 24, 3);
INSERT INTO `tb_question_opt` VALUES (98, '*c=”strin9”;', 24, 4);
INSERT INTO `tb_question_opt` VALUES (99, '7', 25, 1);
INSERT INTO `tb_question_opt` VALUES (100, '8', 25, 2);
INSERT INTO `tb_question_opt` VALUES (101, '9', 25, 3);
INSERT INTO `tb_question_opt` VALUES (102, '10', 25, 4);
INSERT INTO `tb_question_opt` VALUES (103, 'a=+6', 26, 1);
INSERT INTO `tb_question_opt` VALUES (104, '’cd’', 26, 2);
INSERT INTO `tb_question_opt` VALUES (105, '”、a”', 26, 3);
INSERT INTO `tb_question_opt` VALUES (106, '’\\011', 26, 4);
INSERT INTO `tb_question_opt` VALUES (107, '\'a\' && \'b\'', 27, 1);
INSERT INTO `tb_question_opt` VALUES (108, 'a>b', 27, 2);
INSERT INTO `tb_question_opt` VALUES (109, 'a || b+c && b-c', 27, 3);
INSERT INTO `tb_question_opt` VALUES (110, '!(a<b)', 27, 4);
INSERT INTO `tb_question_opt` VALUES (111, '只能写不能读', 55, 1);
INSERT INTO `tb_question_opt` VALUES (112, '写操作结束后可以从头开始读', 55, 2);
INSERT INTO `tb_question_opt` VALUES (113, '可以在原有内容后追加写', 55, 3);
INSERT INTO `tb_question_opt` VALUES (114, '可以随意读和写', 55, 4);
INSERT INTO `tb_question_opt` VALUES (115, 'c程序函数中定义的自动变量，系统不自动赋确定的初值', 56, 1);
INSERT INTO `tb_question_opt` VALUES (116, '在C程序的同一函数中，各复合语句内可以定义变量，其作用域仅限本复合语句内', 56, 2);
INSERT INTO `tb_question_opt` VALUES (117, 'c程序函数中定义的赋有初值的静态变量，每调用一次函数，赋一次初值', 56, 3);
INSERT INTO `tb_question_opt` VALUES (118, 'c程序函数的形参不可以说明为static型变量', 56, 4);
INSERT INTO `tb_question_opt` VALUES (119, 'char*s;s={f.BOOK!”};', 57, 1);
INSERT INTO `tb_question_opt` VALUES (120, 'char*s;s：”BOOK!”：', 57, 2);
INSERT INTO `tb_question_opt` VALUES (121, 'chars[10];s=”BOOK!”;', 57, 3);
INSERT INTO `tb_question_opt` VALUES (122, 'cbars[];s：”BOOK!n：', 57, 4);
INSERT INTO `tb_question_opt` VALUES (123, '函数的形参和实参分别占用不同的存储单元', 58, 1);
INSERT INTO `tb_question_opt` VALUES (124, '.形参只是形式上的存在，不占用具体存储单元', 58, 2);
INSERT INTO `tb_question_opt` VALUES (125, '同名的实参和形参占同一存储单元', 58, 3);
INSERT INTO `tb_question_opt` VALUES (126, '函数的实参和其对应的形参共占同一存储单元', 58, 4);
INSERT INTO `tb_question_opt` VALUES (127, '12345ABCDE', 59, 1);
INSERT INTO `tb_question_opt` VALUES (128, 'EDCBA54321', 59, 2);
INSERT INTO `tb_question_opt` VALUES (129, 'ABCDE12345', 59, 3);
INSERT INTO `tb_question_opt` VALUES (130, '54321EDCBA', 59, 4);
INSERT INTO `tb_question_opt` VALUES (131, '循环队列有队头和队尾两个指针，因此，循环队列是非线性结构', 60, 1);
INSERT INTO `tb_question_opt` VALUES (132, '在循环队列中，只需要队头指针就能反映队列中元素的动态变化情况', 60, 2);
INSERT INTO `tb_question_opt` VALUES (133, '在循环队列中，只需要队尾指针就能反映队列中元素的动态变化情况', 60, 3);
INSERT INTO `tb_question_opt` VALUES (134, '循环队列中元素的个数是由队头指针和队尾指针共同决定的', 60, 4);
INSERT INTO `tb_question_opt` VALUES (135, 'O(n)', 61, 1);
INSERT INTO `tb_question_opt` VALUES (136, 'O(n2)', 61, 2);
INSERT INTO `tb_question_opt` VALUES (137, 'O(nlog2n)', 61, 3);
INSERT INTO `tb_question_opt` VALUES (138, 'O(log2n)', 61, 4);
INSERT INTO `tb_question_opt` VALUES (139, '顺序存储结构的存储一定是连续的，链式存储结构的存储空间不一定是连续的', 62, 1);
INSERT INTO `tb_question_opt` VALUES (140, '顺序存储结构只针对线性结构，链式存储结构只针对非线性结构', 62, 2);
INSERT INTO `tb_question_opt` VALUES (141, '顺序存储结构能存储有序表，链式存储结构不能存储有序表', 62, 3);
INSERT INTO `tb_question_opt` VALUES (142, '链式存储结构比顺序存储结构节省存储空间', 62, 4);
INSERT INTO `tb_question_opt` VALUES (143, '控制流', 63, 1);
INSERT INTO `tb_question_opt` VALUES (144, '事件驱动', 63, 2);
INSERT INTO `tb_question_opt` VALUES (145, '模块调用', 63, 3);
INSERT INTO `tb_question_opt` VALUES (146, '数据流', 63, 4);
INSERT INTO `tb_question_opt` VALUES (147, 'N-S图', 64, 1);
INSERT INTO `tb_question_opt` VALUES (148, 'DFD图', 64, 2);
INSERT INTO `tb_question_opt` VALUES (149, 'PAD图', 64, 3);
INSERT INTO `tb_question_opt` VALUES (150, '程序流程图', 64, 4);
INSERT INTO `tb_question_opt` VALUES (151, '一致性', 65, 1);
INSERT INTO `tb_question_opt` VALUES (152, '分类性', 65, 2);
INSERT INTO `tb_question_opt` VALUES (153, '多态性', 65, 3);
INSERT INTO `tb_question_opt` VALUES (154, '标识唯一性', 65, 4);
INSERT INTO `tb_question_opt` VALUES (155, '一对一', 66, 1);
INSERT INTO `tb_question_opt` VALUES (156, '一对多', 66, 2);
INSERT INTO `tb_question_opt` VALUES (157, '多对一', 66, 3);
INSERT INTO `tb_question_opt` VALUES (158, '多对多', 66, 4);
INSERT INTO `tb_question_opt` VALUES (159, '人工管理阶段', 67, 1);
INSERT INTO `tb_question_opt` VALUES (160, '文件系统阶段', 67, 2);
INSERT INTO `tb_question_opt` VALUES (161, '数据库系统阶段', 67, 3);
INSERT INTO `tb_question_opt` VALUES (162, '三个阶段相同', 67, 4);
INSERT INTO `tb_question_opt` VALUES (163, 'C程序可以由多个程序文件组成', 68, 1);
INSERT INTO `tb_question_opt` VALUES (164, '一个C语言程序只能实现一种算法', 68, 2);
INSERT INTO `tb_question_opt` VALUES (165, 'C程序可以由一个或多个函数组成', 68, 3);
INSERT INTO `tb_question_opt` VALUES (166, '一个C函数可以单独作为一个C程序文件存在', 68, 4);
INSERT INTO `tb_question_opt` VALUES (167, '可以是任意合法的表达式', 69, 1);
INSERT INTO `tb_question_opt` VALUES (168, '只能是整数0或非0整数', 69, 2);
INSERT INTO `tb_question_opt` VALUES (169, '可以是结构体类型的数据', 69, 3);
INSERT INTO `tb_question_opt` VALUES (170, '只能是整数0或1', 69, 4);
INSERT INTO `tb_question_opt` VALUES (171, 'i=a%11;', 70, 1);
INSERT INTO `tb_question_opt` VALUES (172, 'a=a++，i++;', 70, 2);
INSERT INTO `tb_question_opt` VALUES (173, 'i=!a;', 70, 3);
INSERT INTO `tb_question_opt` VALUES (174, '.i=(a+k)<=(i+k);', 70, 4);
INSERT INTO `tb_question_opt` VALUES (175, 'c语言中的每条可执行语句和非执行语句最终都将被转换成二进制的机器指令', 71, 1);
INSERT INTO `tb_question_opt` VALUES (176, 'c程序经过编译、连接步骤之后才能形成一个真正可执行的二进制机器指令文件', 71, 2);
INSERT INTO `tb_question_opt` VALUES (177, '用c语言编写的程序称为源程序，它以ASCIl代码形式存放在一个文本文件中', 71, 3);
INSERT INTO `tb_question_opt` VALUES (178, 'c语言源程序经编译后生成后缀为.obj的目标程序', 71, 4);
INSERT INTO `tb_question_opt` VALUES (179, '&a', 72, 1);
INSERT INTO `tb_question_opt` VALUES (180, 'FOR', 72, 2);
INSERT INTO `tb_question_opt` VALUES (181, 'pfint', 72, 3);
INSERT INTO `tb_question_opt` VALUES (182, '0', 72, 4);
INSERT INTO `tb_question_opt` VALUES (183, '当对文件的读(写)操作完成之后，必须将它关闭，否则可能导致数据丢失', 73, 1);
INSERT INTO `tb_question_opt` VALUES (184, '打开一个已存在的文件并进行了写操作后，原有文件中的全部数据必定被覆盖', 73, 2);
INSERT INTO `tb_question_opt` VALUES (185, '在一个程序中当对文件进行了写操作后，必须先关闭该文件然后再打开，才能读到第1个数据', 73, 3);
INSERT INTO `tb_question_opt` VALUES (186, 'c语言中的文件是流式文件，因此只能顺序存取数据', 73, 4);
INSERT INTO `tb_question_opt` VALUES (187, '由具体定义位置和extem说明来决定范围', 74, 1);
INSERT INTO `tb_question_opt` VALUES (188, '所在程序的全部范围', 74, 2);
INSERT INTO `tb_question_opt` VALUES (189, '所在函数的全部范围', 74, 3);
INSERT INTO `tb_question_opt` VALUES (190, '所在文件的全部范围', 74, 4);
INSERT INTO `tb_question_opt` VALUES (191, '可以通过typedef增加新的类型', 75, 1);
INSERT INTO `tb_question_opt` VALUES (192, '可以用typedef将已存在的类型用一个新的名字来代表', 75, 2);
INSERT INTO `tb_question_opt` VALUES (193, '用typedef定义新的类型名后，原有类型名仍有效', 75, 3);
INSERT INTO `tb_question_opt` VALUES (194, '用typedef可以为各种类型起别名，但不能为变量起别名', 75, 4);
INSERT INTO `tb_question_opt` VALUES (195, 'do{*t++=*8++;}while(*s);', 76, 1);
INSERT INTO `tb_question_opt` VALUES (196, 'for(i=0;t[i]=s[i];i++);', 76, 2);
INSERT INTO `tb_question_opt` VALUES (197, 'while(*t=*s){t++;s++;}', 76, 3);
INSERT INTO `tb_question_opt` VALUES (198, 'for(i=0，j=0;t[i++]=s[j++];);', 76, 4);
INSERT INTO `tb_question_opt` VALUES (199, '栈是“先进先出”的线性表', 77, 1);
INSERT INTO `tb_question_opt` VALUES (200, '队列是“先进后出”的线性表', 77, 2);
INSERT INTO `tb_question_opt` VALUES (201, '循环队列是非线性结构', 77, 3);
INSERT INTO `tb_question_opt` VALUES (202, '有序线性表既可以采用顺序存储结构，也可以采用链式存储结构', 77, 4);
INSERT INTO `tb_question_opt` VALUES (203, '栈', 78, 1);
INSERT INTO `tb_question_opt` VALUES (204, '树', 78, 2);
INSERT INTO `tb_question_opt` VALUES (205, '队列', 78, 3);
INSERT INTO `tb_question_opt` VALUES (206, '二叉树', 78, 4);
INSERT INTO `tb_question_opt` VALUES (207, '10', 79, 1);
INSERT INTO `tb_question_opt` VALUES (208, '8', 79, 2);
INSERT INTO `tb_question_opt` VALUES (209, '6', 79, 3);
INSERT INTO `tb_question_opt` VALUES (210, '4', 79, 4);
INSERT INTO `tb_question_opt` VALUES (211, '冒泡排序', 80, 1);
INSERT INTO `tb_question_opt` VALUES (212, '简单选择排序', 80, 2);
INSERT INTO `tb_question_opt` VALUES (213, '直接插入排序', 80, 3);
INSERT INTO `tb_question_opt` VALUES (214, '堆排序', 80, 4);
INSERT INTO `tb_question_opt` VALUES (215, '同呼吸', 81, 1);
INSERT INTO `tb_question_opt` VALUES (216, '共命运', 81, 2);
INSERT INTO `tb_question_opt` VALUES (217, '手牵手', 81, 3);
INSERT INTO `tb_question_opt` VALUES (218, '心连心', 81, 4);
INSERT INTO `tb_question_opt` VALUES (219, '对外贸易', 82, 1);
INSERT INTO `tb_question_opt` VALUES (220, '对外投资', 82, 2);
INSERT INTO `tb_question_opt` VALUES (221, '外汇储备', 82, 3);
INSERT INTO `tb_question_opt` VALUES (222, '外汇支出', 82, 4);
INSERT INTO `tb_question_opt` VALUES (223, '法治生活', 83, 1);
INSERT INTO `tb_question_opt` VALUES (224, '法治国家', 83, 2);
INSERT INTO `tb_question_opt` VALUES (225, '法治政府　', 83, 3);
INSERT INTO `tb_question_opt` VALUES (226, '法治社会', 83, 4);
INSERT INTO `tb_question_opt` VALUES (227, '参与者', 84, 1);
INSERT INTO `tb_question_opt` VALUES (228, '贡献者', 84, 2);
INSERT INTO `tb_question_opt` VALUES (229, '引领者', 84, 3);
INSERT INTO `tb_question_opt` VALUES (230, '领导者', 84, 4);
INSERT INTO `tb_question_opt` VALUES (231, '形式主义', 85, 1);
INSERT INTO `tb_question_opt` VALUES (232, '官僚主义', 85, 2);
INSERT INTO `tb_question_opt` VALUES (233, '享乐主义', 85, 3);
INSERT INTO `tb_question_opt` VALUES (234, '奢靡之风', 85, 4);
INSERT INTO `tb_question_opt` VALUES (235, '个人主义', 85, 5);
INSERT INTO `tb_question_opt` VALUES (236, '创造力', 86, 1);
INSERT INTO `tb_question_opt` VALUES (237, '创新力', 86, 2);
INSERT INTO `tb_question_opt` VALUES (238, '凝聚力', 86, 3);
INSERT INTO `tb_question_opt` VALUES (239, '战斗力', 86, 4);
INSERT INTO `tb_question_opt` VALUES (240, '照镜子', 87, 1);
INSERT INTO `tb_question_opt` VALUES (241, '正衣冠', 87, 2);
INSERT INTO `tb_question_opt` VALUES (242, '洗洗澡', 87, 3);
INSERT INTO `tb_question_opt` VALUES (243, '治治病', 87, 4);
INSERT INTO `tb_question_opt` VALUES (244, '承前启后、继往开来、在新的历史条件下继续夺取中国特色社会主义伟大胜利的时代', 88, 1);
INSERT INTO `tb_question_opt` VALUES (245, '决胜全面建成小康社会、进而全面建设社会主义现代化强国的时代', 88, 2);
INSERT INTO `tb_question_opt` VALUES (246, '全国各族人民团结奋斗、不断创造美好生活、逐步实现全体人民共同富裕的时代', 88, 3);
INSERT INTO `tb_question_opt` VALUES (247, '全体中华儿女勠力同心、奋力实现中华民族伟大复兴中国梦的时代', 88, 4);
INSERT INTO `tb_question_opt` VALUES (248, '我国日益走近世界舞台中央、不断为人类作出更大贡献的时代', 88, 5);
INSERT INTO `tb_question_opt` VALUES (249, '道路自信', 89, 1);
INSERT INTO `tb_question_opt` VALUES (250, '理论自信　', 89, 2);
INSERT INTO `tb_question_opt` VALUES (251, '制度自信', 89, 3);
INSERT INTO `tb_question_opt` VALUES (252, '文化自信', 89, 4);
INSERT INTO `tb_question_opt` VALUES (253, '思想自信', 89, 5);
INSERT INTO `tb_question_opt` VALUES (254, '对马克思列宁主义、毛泽东思想、邓小平理论、“三个代表”重要思想、科学发展观的继承和发展', 90, 1);
INSERT INTO `tb_question_opt` VALUES (255, '马克思主义中国化最新成果', 90, 2);
INSERT INTO `tb_question_opt` VALUES (256, '党和人民实践经验和集体智慧的结晶', 90, 3);
INSERT INTO `tb_question_opt` VALUES (257, '中国特色社会主义理论体系的重要组成部分', 90, 4);
INSERT INTO `tb_question_opt` VALUES (258, '全党全国人民为实现中华民族伟大复兴而奋斗的行动指南', 90, 5);
INSERT INTO `tb_question_opt` VALUES (259, '坚持党对一切工作的领导，坚持以人民为中心，坚持全面深化改革', 91, 1);
INSERT INTO `tb_question_opt` VALUES (260, '坚持新发展理念，坚持人民当家作主，坚持全面依法治国', 91, 2);
INSERT INTO `tb_question_opt` VALUES (261, '坚持社会主义核心价值体系，坚持在发展中保障和改善民生，坚持人与自然和谐共生', 91, 3);
INSERT INTO `tb_question_opt` VALUES (262, '坚持总体国家安全观，坚持党对人民军队的绝对领导，坚持“一国两制”和推进祖国统一', 91, 4);
INSERT INTO `tb_question_opt` VALUES (263, '坚持推动构建人类命运共同体，坚持全面从严治党', 91, 5);
INSERT INTO `tb_question_opt` VALUES (264, '政治意识', 92, 1);
INSERT INTO `tb_question_opt` VALUES (265, '大局意识', 92, 2);
INSERT INTO `tb_question_opt` VALUES (266, '核心意识', 92, 3);
INSERT INTO `tb_question_opt` VALUES (267, '看齐意识', 92, 4);
INSERT INTO `tb_question_opt` VALUES (268, '纪律意识', 92, 5);
INSERT INTO `tb_question_opt` VALUES (269, '听党指挥', 93, 1);
INSERT INTO `tb_question_opt` VALUES (270, '骁勇善战', 93, 2);
INSERT INTO `tb_question_opt` VALUES (271, '能打胜仗', 93, 3);
INSERT INTO `tb_question_opt` VALUES (272, '作风优良', 93, 4);
INSERT INTO `tb_question_opt` VALUES (273, '农业', 94, 1);
INSERT INTO `tb_question_opt` VALUES (274, '农村', 94, 2);
INSERT INTO `tb_question_opt` VALUES (275, '农民', 94, 3);
INSERT INTO `tb_question_opt` VALUES (276, '农田', 94, 4);
INSERT INTO `tb_question_opt` VALUES (277, '长期共存', 95, 1);
INSERT INTO `tb_question_opt` VALUES (278, '互相监督', 95, 2);
INSERT INTO `tb_question_opt` VALUES (279, '肝胆相照', 95, 3);
INSERT INTO `tb_question_opt` VALUES (280, '荣辱与共', 95, 4);
INSERT INTO `tb_question_opt` VALUES (281, '扩大中等收入群体', 96, 1);
INSERT INTO `tb_question_opt` VALUES (282, '增加低收入者收入', 96, 2);
INSERT INTO `tb_question_opt` VALUES (283, '调节过高收入', 96, 3);
INSERT INTO `tb_question_opt` VALUES (284, '取缔非法收入', 96, 4);
INSERT INTO `tb_question_opt` VALUES (285, '多主体供给', 97, 1);
INSERT INTO `tb_question_opt` VALUES (286, '多渠道保障', 97, 2);
INSERT INTO `tb_question_opt` VALUES (287, '租购并举', 97, 3);
INSERT INTO `tb_question_opt` VALUES (288, '多部门监管', 97, 4);
INSERT INTO `tb_question_opt` VALUES (289, '社会化', 98, 1);
INSERT INTO `tb_question_opt` VALUES (290, '法治化', 98, 2);
INSERT INTO `tb_question_opt` VALUES (291, '智能化', 98, 3);
INSERT INTO `tb_question_opt` VALUES (292, '专业化', 98, 4);
INSERT INTO `tb_question_opt` VALUES (293, '事先预防', 99, 1);
INSERT INTO `tb_question_opt` VALUES (294, '节约优先', 99, 2);
INSERT INTO `tb_question_opt` VALUES (295, '保护优先', 99, 3);
INSERT INTO `tb_question_opt` VALUES (296, '自然恢复', 99, 4);
INSERT INTO `tb_question_opt` VALUES (297, '节能环保产业', 100, 1);
INSERT INTO `tb_question_opt` VALUES (298, '清洁生产产业', 100, 2);
INSERT INTO `tb_question_opt` VALUES (299, '绿色科技产业', 100, 3);
INSERT INTO `tb_question_opt` VALUES (300, '清洁能源产业', 100, 4);
INSERT INTO `tb_question_opt` VALUES (301, '绿色家庭', 101, 1);
INSERT INTO `tb_question_opt` VALUES (302, '绿色学校', 101, 2);
INSERT INTO `tb_question_opt` VALUES (303, '绿色社区', 101, 3);
INSERT INTO `tb_question_opt` VALUES (304, '绿色城市', 101, 4);
INSERT INTO `tb_question_opt` VALUES (305, '绿色出行', 101, 5);
INSERT INTO `tb_question_opt` VALUES (306, '环保信用评价', 102, 1);
INSERT INTO `tb_question_opt` VALUES (307, '污染企业备案', 102, 2);
INSERT INTO `tb_question_opt` VALUES (308, '信息强制性披露', 102, 3);
INSERT INTO `tb_question_opt` VALUES (309, '严惩重罚', 102, 4);
INSERT INTO `tb_question_opt` VALUES (310, '生态保护红线', 103, 1);
INSERT INTO `tb_question_opt` VALUES (311, '永久基本农田', 103, 2);
INSERT INTO `tb_question_opt` VALUES (312, '城镇开发边界', 103, 3);
INSERT INTO `tb_question_opt` VALUES (313, '国土绿化面积', 103, 4);
INSERT INTO `tb_question_opt` VALUES (314, '“一国两制”　', 104, 1);
INSERT INTO `tb_question_opt` VALUES (315, '“港人治港”', 104, 2);
INSERT INTO `tb_question_opt` VALUES (316, '“澳人治澳”', 104, 3);
INSERT INTO `tb_question_opt` VALUES (317, '高度自治', 104, 4);
INSERT INTO `tb_question_opt` VALUES (318, '粤港澳大湾区建设', 105, 1);
INSERT INTO `tb_question_opt` VALUES (319, '粤港澳合作', 105, 2);
INSERT INTO `tb_question_opt` VALUES (320, '粤港澳政府合作', 105, 3);
INSERT INTO `tb_question_opt` VALUES (321, '泛珠三角区域合作', 105, 4);
INSERT INTO `tb_question_opt` VALUES (322, '和平', 106, 1);
INSERT INTO `tb_question_opt` VALUES (323, '发展　', 106, 2);
INSERT INTO `tb_question_opt` VALUES (324, '合作', 106, 3);
INSERT INTO `tb_question_opt` VALUES (325, '共赢', 106, 4);
INSERT INTO `tb_question_opt` VALUES (326, '互惠', 106, 5);
INSERT INTO `tb_question_opt` VALUES (327, '相互尊重', 107, 1);
INSERT INTO `tb_question_opt` VALUES (328, '公平正义', 107, 2);
INSERT INTO `tb_question_opt` VALUES (329, '互不干涉', 107, 3);
INSERT INTO `tb_question_opt` VALUES (330, '合作共赢', 107, 4);
INSERT INTO `tb_question_opt` VALUES (331, '世界多极化', 108, 1);
INSERT INTO `tb_question_opt` VALUES (332, '经济全球化', 108, 2);
INSERT INTO `tb_question_opt` VALUES (333, '社会信息化', 108, 3);
INSERT INTO `tb_question_opt` VALUES (334, '文化多样化', 108, 4);
INSERT INTO `tb_question_opt` VALUES (335, '治理民主化', 108, 5);
INSERT INTO `tb_question_opt` VALUES (336, '政策沟通', 109, 1);
INSERT INTO `tb_question_opt` VALUES (337, '设施联通', 109, 2);
INSERT INTO `tb_question_opt` VALUES (338, '贸易畅通', 109, 3);
INSERT INTO `tb_question_opt` VALUES (339, '人员互通', 109, 4);
INSERT INTO `tb_question_opt` VALUES (340, '资金融通', 109, 5);
INSERT INTO `tb_question_opt` VALUES (341, '民心相通', 109, 6);
INSERT INTO `tb_question_opt` VALUES (342, '执政考验', 110, 1);
INSERT INTO `tb_question_opt` VALUES (343, '改革开放考验', 110, 2);
INSERT INTO `tb_question_opt` VALUES (344, '市场经济考验', 110, 3);
INSERT INTO `tb_question_opt` VALUES (345, '生态保护考验', 110, 4);
INSERT INTO `tb_question_opt` VALUES (346, '外部环境考验', 110, 5);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `base_per` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '基础权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '超级管理员', '1');
INSERT INTO `tb_role` VALUES (2, '管理员', '6');
INSERT INTO `tb_role` VALUES (3, '教师', '3,5,6');
INSERT INTO `tb_role` VALUES (4, '学生', '2');

-- ----------------------------
-- Table structure for tb_student
-- ----------------------------
DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `grade_id` int(11) NOT NULL COMMENT '年级',
  `major_id` int(11) NOT NULL COMMENT '专业',
  `classes` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `institute` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学院',
  `id_card` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_student
-- ----------------------------
INSERT INTO `tb_student` VALUES (2, 5, 1, 1, '二班', '信息', '222222222', '男', '无');

-- ----------------------------
-- Table structure for tb_student_grade
-- ----------------------------
DROP TABLE IF EXISTS `tb_student_grade`;
CREATE TABLE `tb_student_grade`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grade_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `grade_name`(`grade_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_student_grade
-- ----------------------------
INSERT INTO `tb_student_grade` VALUES (1, '2015');
INSERT INTO `tb_student_grade` VALUES (2, '2016');
INSERT INTO `tb_student_grade` VALUES (3, '2017');
INSERT INTO `tb_student_grade` VALUES (4, '2018');
INSERT INTO `tb_student_grade` VALUES (5, '2019');
INSERT INTO `tb_student_grade` VALUES (6, '2020');
INSERT INTO `tb_student_grade` VALUES (7, '2021');
INSERT INTO `tb_student_grade` VALUES (8, '2022');
INSERT INTO `tb_student_grade` VALUES (9, '2023');
INSERT INTO `tb_student_grade` VALUES (10, '2024');
INSERT INTO `tb_student_grade` VALUES (11, '2025');
INSERT INTO `tb_student_grade` VALUES (12, '2026');
INSERT INTO `tb_student_grade` VALUES (13, '2027');
INSERT INTO `tb_student_grade` VALUES (14, '2028');
INSERT INTO `tb_student_grade` VALUES (15, '2029');
INSERT INTO `tb_student_grade` VALUES (16, '2030');

-- ----------------------------
-- Table structure for tb_student_major
-- ----------------------------
DROP TABLE IF EXISTS `tb_student_major`;
CREATE TABLE `tb_student_major`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `major_name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '专业名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `major_name`(`major_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_student_major
-- ----------------------------
INSERT INTO `tb_student_major` VALUES (1, '计算机');
INSERT INTO `tb_student_major` VALUES (2, '软件工程');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` int(11) NOT NULL,
  `permissions` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_uqe_idx`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', 'a1ee9531241e2fe8f5b386b22dfab746', 'admin', '123456', '123456', 1, '2');
INSERT INTO `tb_user` VALUES (2, 'admin1', '7f1a3a9dc28d47e5849c3c14ba6606c4', 'admin', '123456', '123456', 2, '2');
INSERT INTO `tb_user` VALUES (3, 'admin2', 'a1ee9531241e2fe8f5b386b22dfab746', 'admin', '123456', '123456', 3, '2');
INSERT INTO `tb_user` VALUES (5, '201511010125', 'a1ee9531241e2fe8f5b386b22dfab746', 'student', '1234566', '无', 4, '2');

SET FOREIGN_KEY_CHECKS = 1;
