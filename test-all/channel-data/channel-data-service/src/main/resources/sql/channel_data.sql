CREATE TABLE `channel_data` (
  `id` bigint NOT NULL COMMENT 
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `introduce` varchar(500) DEFAULT NULL COMMENT '介绍描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT '1' COMMENT '状态 1:启用 -1:禁用'
  `public_key` text CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'rsa公钥',
  `secret_key` text CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'rsa密码',
  `aes_key` text CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'aes秘钥',
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道基础数据信息';