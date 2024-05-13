package com.damai.jwt;

import com.alibaba.fastjson.JSONObject;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: token工具
 * @author: 阿星不是程序员
 **/
@Slf4j
public class TokenUtil {
    
     private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    
    public static String createToken(String id, String info, long ttlMillis, String tokenSecret) {
        
        long nowMillis = System.currentTimeMillis();
        
       
        JwtBuilder builder = Jwts.builder()
//                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(nowMillis))
                .setSubject(info)
                .signWith(SIGNATURE_ALGORITHM, tokenSecret);
        if (ttlMillis >= 0) {
            //设置过期时间
            builder.setExpiration(new Date(nowMillis + ttlMillis));
        }
        return builder.compact();
    }

    public static String parseToken(String token, String tokenSecret) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException jwtException) {
            log.error("parseToken error",jwtException);
            throw new DaMaiFrameException(BaseCode.TOKEN_EXPIRE);
        }
        
    }
    
    public static void main(String[] args) {
        
         String tokenSecret = "CSYZWECHAT";
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("001key", "001value");
        jsonObject.put("002key", "001value");

        String token1 = TokenUtil.createToken("1", jsonObject.toJSONString(), 10000, tokenSecret);
        System.out.println("token:" + token1);
        
        
        String token2 = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjg4NTQyODM3LCJzdWIiOiJ7XCIwMDJrZXlcIjpcIjAwMXZhbHVlXCIsXCIwMDFrZXlcIjpcIjAwMXZhbHVlXCJ9IiwiZXhwIjoxNjg4NTQyODQ3fQ.vIKcAilTn_CR3VYssNE7rBpfuCSCH_RrkmsadLWf664";
        String subject = TokenUtil.parseToken(token2, tokenSecret);
        System.out.println("解析token后的值:" + subject);
    }
}