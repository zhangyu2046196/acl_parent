package com.youyuan.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 类名称：TokenManager <br>
 * 类描述： 生成和解析token工具 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 15:47<br>
 */
@Component
public class TokenManager {

    /**
     * 设置token过期时间信息
     */
    private static final Integer tokenExpiretion = 24 * 60 * 60 * 1000;
    /**
     * 设置token编码秘钥
     */
    private static final String tokenSignKey = "youyuan123";

    /**
     * 方法名: createToken <br>
     * 方法描述: 使用jwt框架对用户名生成token <br>
     *
     * @param userName 用户名
     * @return {@link String 返回生成token数据 }
     * @date 创建时间: 2021/9/19 15:50 <br>
     * @author zhangyu
     */
    public String createToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiretion))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
    }

    /**
     * 方法名: getUserInfoByToken <br>
     * 方法描述: 根据token获取用户信息 <br>
     *
     * @param token token信息
     * @return {@link String 返回用户信息 }
     * @date 创建时间: 2021/9/19 15:57 <br>
     * @author zhangyu
     */
    public String getUserInfoByToken(String token) {
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
    * 方法名: removeToken <br>
    * 方法描述: 删除token <br>
    *
     * @param token token信息
    * @date 创建时间: 2021/9/19 16:01 <br>
    * @author zhangyu
    */
    public void removeToken(String token){

    }

}
