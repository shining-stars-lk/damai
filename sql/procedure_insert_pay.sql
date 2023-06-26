DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `toolkit`.`insert_pay`()
BEGIN
   	DECLARE paramter_user_id VARCHAR(32);
   	DECLARE paramter_id int;
	DECLARE i INT DEFAULT 5;

   WHILE i <= 9000 DO
     SET paramter_user_id = CONCAT(i, "12345");

     SET paramter_id = i;


INSERT INTO  `pay` (
    id, user_id, type, status, amount, origin, pay_time, department_id, create_time, refund_mark
)
VALUES(paramter_id,
       paramter_user_id,
       1,
       2,
       round(rand() * 100, 0),
       1,
       now(),
       paramter_id,
       now(),
       '测试备注'
      );
SET i=i+1;
END WHILE;
end //
DELIMITER ;