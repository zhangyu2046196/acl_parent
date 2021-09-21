package com.youyuan.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youyuan.entity.SecurityUser;
import com.youyuan.entity.User;
import com.youyuan.security.TokenManager;
import com.youyuan.utils.R;
import com.youyuan.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 类名称：TokenLoginFilter <br>
 * 类描述： 自定义认证过滤器 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 19:42<br>
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(TokenManager tokenManager, RedisTemplate redisTemplate, AuthenticationManager
            authenticationManager) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
        this.setPostOnly(Boolean.FALSE);
        //指定登录地址及提交方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/acl/login", "POST"));
    }

    /**
     * 方法名: attemptAuthentication <br>
     * 方法描述: 从表单获取用户名和密码 <br>
     *
     * @param request  请求参数数据
     * @param response 返回参数数据
     * @return {@link Authentication 返回认证结果信息 }
     * @date 创建时间: 2021/9/19 19:43 <br>
     * @author zhangyu
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException {
        //获取表单提交数据
        try {
            //user为数据库对应的实体
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user
                    .getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法名: successfulAuthentication <br>
     * 方法描述: 认证成功方法 <br>
     *
     * @param request    请求参数数据
     * @param response   返回参数数据
     * @param authResult 认证结果信息
     * @date 创建时间: 2021/9/19 19:44 <br>
     * @author zhangyu
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) throws IOException, ServletException {
        //获取认证成功对象
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
        //根据用户名创建token
        String token = tokenManager.createToken(securityUser.getCurrentUserInfo().getUsername());
        //redis中存储key为用户名 value是权限列表
        redisTemplate.opsForValue().set(securityUser.getCurrentUserInfo().getUsername(), securityUser
                .getPermissionValueList());
        //把token写入cookie
        ResponseUtil.out(response, R.ok().data("token", token));
    }

    /**
     * 方法名: unsuccessfulAuthentication <br>
     * 方法描述: 认证失败方法 <br>
     *
     * @param request  请求参数数据
     * @param response 返回参数数据
     * @param failed   认证失败异常
     * @date 创建时间: 2021/9/19 19:45 <br>
     * @author zhangyu
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }
}
