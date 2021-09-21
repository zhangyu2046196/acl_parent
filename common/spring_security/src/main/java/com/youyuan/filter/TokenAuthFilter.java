package com.youyuan.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.youyuan.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 类名称：TokenAuthFilter <br>
 * 类描述： 自定义授权过滤器 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 20:28<br>
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate
            redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 方法名: doFilterInternal <br>
     * 方法描述: 认证成功对象获取权限列表放到上下文中 <br>
     *
     * @param request  请求参数信息
     * @param response 返回参数信息
     * @param chain    过滤器
     * @date 创建时间: 2021/9/19 20:30 <br>
     * @author zhangyu
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //获取认证用户
        UsernamePasswordAuthenticationToken authResult = getAuthentication(request);
        //判断如果权限信息不为空放到上下文中
        if (ObjectUtil.isNotEmpty(authResult)) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        //放行操作内容
        chain.doFilter(request, response);
    }

    /**
     * 方法名: getAuthentication <br>
     * 方法描述: 认证用户解析权限列表 <br>
     *
     * @param request 请求数据信息
     * @return {@link UsernamePasswordAuthenticationToken 返回权限列表对象 }
     * @date 创建时间: 2021/9/19 20:34 <br>
     * @author zhangyu
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //请求头获取token
        String token = request.getParameter("token");
        if (StrUtil.isNotBlank(token)) {
            //解析token获取username
            String userName = tokenManager.getUserInfoByToken(token);
            //redis根据userName查询权限列表
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(userName);
            //权限转成指定结构
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            permissionValueList.stream().forEach(permiss -> authorities.add(new SimpleGrantedAuthority(permiss)));
            return new UsernamePasswordAuthenticationToken(userName, token, authorities);
        }
        return null;
    }
}
