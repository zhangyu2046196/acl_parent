package com.youyuan.security;

import com.youyuan.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 类名称：DefaultPasswordEncoder <br>
 * 类描述：加密工具类 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 15:18<br>
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {


    /**
     * 方法名: encode <br>
     * 方法描述: 进行MD5加密 <br>
     *
     * @return {@link String 返回加密后的字符串 }
     * @date 创建时间: 2021/9/19 15:20 <br>
     * @author zhangyu
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    /**
     * 方法名: matches <br>
     * 方法描述: 加密密码和之前密码比对 <br>
     *
     * @return {@link boolean true比对成功 false比对失败}
     * @date 创建时间: 2021/9/19 15:21 <br>
     * @author zhangyu
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(encode(rawPassword), encodedPassword);
    }
}
