CREATE DATABASE `toolkit`;
-- toolkit.channel_data definition

CREATE TABLE `channel_data` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `code` varchar(50) DEFAULT NULL COMMENT '编码',
  `introduce` varchar(500) DEFAULT NULL COMMENT '介绍描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT '1' COMMENT '状态 1:启用 -1:禁用',
  `public_key` text COMMENT 'rsa公钥',
  `secret_key` text COMMENT 'rsa密码',
  `aes_key` text COMMENT 'aes秘钥',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道基础数据信息';


-- toolkit.department definition

CREATE TABLE `department` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `type_code` varchar(50) DEFAULT NULL COMMENT '分类编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT '1' COMMENT '状态 1:启用 -1:禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息';


-- toolkit.pay definition

CREATE TABLE `pay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '[ID]',
  `user_id` varchar(32) DEFAULT NULL COMMENT '[用户ID]',
  `type` int(2) DEFAULT NULL COMMENT '[支付方式]1.支付宝 2.微信',
  `status` int(1) DEFAULT NULL COMMENT '[状态]1:待支付 2：已支付 3:支付失败',
  `amount` decimal(15,4) DEFAULT NULL COMMENT '[支付总金额]',
  `origin` int(1) DEFAULT NULL COMMENT '[支付来源]1:安卓 2:iPhone 3:pc',
  `pay_time` datetime DEFAULT NULL COMMENT '[支付时间]',
  `department_id` varchar(32) DEFAULT NULL COMMENT '[部门id]',
  `create_time` datetime DEFAULT NULL COMMENT '[创建时间]',
  `refund_status` int(1) DEFAULT '1' COMMENT '[退款状态]1.不退款 2.应退款 3.退款成功 -1.退款失败',
  `refund_amount` decimal(15,4) DEFAULT NULL COMMENT '[退款金额]',
  `refund_time` datetime DEFAULT NULL COMMENT '[退款时间]',
  `refund_mark` text COMMENT '[退款原因]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='[支付]';


-- toolkit.product definition

CREATE TABLE `product` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `name` varchar(32) DEFAULT NULL COMMENT '[产品名字]',
  `price` decimal(16,6) DEFAULT NULL COMMENT '[产品单价]',
  `stock` int(11) DEFAULT NULL COMMENT '[库存数量]',
  `status` int(11) DEFAULT '0' COMMENT '[状态 -1:已删除 0:正常]',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';


-- toolkit.product_order definition

CREATE TABLE `product_order` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `product_id` varchar(50) NOT NULL COMMENT '[产品id]',
  `product_name` varchar(32) DEFAULT NULL COMMENT '[产品名字]',
  `product_price` decimal(16,6) DEFAULT NULL COMMENT '[产品单价]',
  `product_amount` int(11) DEFAULT NULL COMMENT '[产品数量]',
  `product_total_price` decimal(16,6) DEFAULT NULL COMMENT '[产品总价]',
  `order_id` varchar(50) NOT NULL COMMENT '[订单id]',
  `status` int(11) DEFAULT '0' COMMENT '[状态 -1:已删除 0:正常]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品订单关联表';


-- toolkit.ps_order definition

CREATE TABLE `ps_order` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `pay_order_id` varchar(32) DEFAULT NULL COMMENT '[支付单ID]ID值来自<支付单表>',
  `pay_amount` decimal(16,6) NOT NULL COMMENT '[缴费金额]',
  `pay_channel_type` int(11) DEFAULT NULL COMMENT '[支付方式1.支付宝 2.微信]',
  `pay_time` datetime DEFAULT NULL COMMENT '[支付完成时间]',
  `status` int(11) DEFAULT '0' COMMENT '[状态 -1:已删除 0:待支付 1:正在支付 2:支付完成 3:超时取消 4:手动取消]',
  `create_time` datetime DEFAULT NULL COMMENT '[创建时间]',
  `user_id` varchar(32) DEFAULT NULL COMMENT '[用户id]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


-- toolkit.test definition

CREATE TABLE `test` (
  `id` bigint(11) NOT NULL,
  `column1` int(11) DEFAULT NULL,
  `column2` int(11) DEFAULT NULL,
  `column3` int(11) DEFAULT NULL,
  `column4` varchar(255) DEFAULT NULL,
  `column5` varchar(255) DEFAULT NULL,
  `column6` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_column1_2_3` (`column1`,`column2`,`column3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- toolkit.`user` definition

CREATE TABLE `user` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL COMMENT '[创建时间]',
  `mobile` varchar(14) DEFAULT NULL COMMENT '手机号',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;