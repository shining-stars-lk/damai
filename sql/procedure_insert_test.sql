DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `toolkit`.`insert_test`()
BEGIN
   	DECLARE paramter_id int;
	DECLARE i INT DEFAULT 5;

   WHILE i <= 15 DO
     SET paramter_id = i;


INSERT INTO  `test` (
    id, column1, column2, column3, column4, column5, column6, number
)
VALUES(paramter_id,
       paramter_id,
       paramter_id,
       paramter_id,
       CONCAT(i, "_4"),
       CONCAT(i, "_5"),
       CONCAT(i, "_6"),
       paramter_id
      );
SET i=i+1;
END WHILE;
end //
DELIMITER ;