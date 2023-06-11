CREATE TABLE `account` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '[ID]',
   `name` varchar(32) DEFAULT NULL COMMENT '[账户名]',
   `password` varchar(32) DEFAULT NULL COMMENT '[密码部门id]',
   `age` int DEFAULT NULL COMMENT '年龄',
   `status` int default 1 COMMENT '状态',
   `create_time` datetime DEFAULT NULL COMMENT '[创建时间]',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='[账户支付]';