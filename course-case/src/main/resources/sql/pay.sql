
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
  `refund_mark` text DEFAULT NULL COMMENT '[退款原因]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='[支付]';