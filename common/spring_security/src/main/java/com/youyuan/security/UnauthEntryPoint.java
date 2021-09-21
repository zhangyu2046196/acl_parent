package com.youyuan.security;

import com.youyuan.utils.R;
import com.youyuan.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类名称：UnauthEntryPoint <br>
 * 类描述： 未授权统一处理类 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 19:27<br>
 */
@Component
public class UnauthEntryPoint implements AuthenticationEntryPoint {

    /**
     * 方法名: commence <br>
     * 方法描述: 未授权统一处理接口 <br>
     *
     * @param request       请求
     * @param response      返回
     * @param authException 异常
     * @date 创建时间: 2021/9/19 19:28 <br>
     * @author zhangyu
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException
            authException) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }
}
