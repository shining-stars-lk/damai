package com.example.servecase.provider;


import com.example.servecase.entity.Pay;
import org.apache.ibatis.jdbc.SQL;

public class PayProvider {

    public String insert(Pay record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("pay");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=VARCHAR}");
        }
        
        if (record.getType() != null) {
            sql.VALUES("type", "#{type,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        if (record.getAmount() != null) {
            sql.VALUES("amount", "#{amount,jdbcType=DECIMAL}");
        }
        
        if (record.getOrigin() != null) {
            sql.VALUES("origin", "#{origin,jdbcType=INTEGER}");
        }
        
        if (record.getPayTime() != null) {
            sql.VALUES("pay_time", "#{payTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getDepartmentId() != null) {
            sql.VALUES("department_id", "#{departmentId,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRefundStatus() != null) {
            sql.VALUES("refund_status", "#{refundStatus,jdbcType=INTEGER}");
        }
        
        if (record.getRefundAmount() != null) {
            sql.VALUES("refund_amount", "#{refundAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getRefundTime() != null) {
            sql.VALUES("refund_time", "#{refundTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRefundMark() != null) {
            sql.VALUES("refund_mark", "#{refundMark,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String updateById(Pay record) {
        SQL sql = new SQL();
        sql.UPDATE("pay");
        
        if (record.getUserId() != null) {
            sql.SET("user_id = #{userId,jdbcType=VARCHAR}");
        }
        
        if (record.getType() != null) {
            sql.SET("type = #{type,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=INTEGER}");
        }
        
        if (record.getAmount() != null) {
            sql.SET("amount = #{amount,jdbcType=DECIMAL}");
        }
        
        if (record.getOrigin() != null) {
            sql.SET("origin = #{origin,jdbcType=INTEGER}");
        }
        
        if (record.getPayTime() != null) {
            sql.SET("pay_time = #{payTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getDepartmentId() != null) {
            sql.SET("department_id = #{departmentId,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRefundStatus() != null) {
            sql.SET("refund_status = #{refundStatus,jdbcType=INTEGER}");
        }
        
        if (record.getRefundAmount() != null) {
            sql.SET("refund_amount = #{refundAmount,jdbcType=DECIMAL}");
        }
        
        if (record.getRefundTime() != null) {
            sql.SET("refund_time = #{refundTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getRefundMark() != null) {
            sql.SET("refund_mark = #{refundMark,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}