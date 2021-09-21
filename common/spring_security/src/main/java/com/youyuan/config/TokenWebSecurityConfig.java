package com.youyuan.config;

import com.youyuan.filter.TokenAuthFilter;
import com.youyuan.filter.TokenLoginFilter;
import com.youyuan.security.DefaultPasswordEncoder;
import com.youyuan.security.TokenLogOutHandler;
import com.youyuan.security.TokenManager;
import com.youyuan.security.UnauthEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 类名称：TokenWebSecurityConfig <br>
 * 类描述： 自定义配置类 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 21:03<br>
 */
@Configuration
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private DefaultPasswordEncoder defaultPasswordEncoder;
    private UserDetailsService userDetailsService;

    public TokenWebSecurityConfig(TokenManager tokenManager, RedisTemplate redisTemplate, DefaultPasswordEncoder
            defaultPasswordEncoder, UserDetailsService userDetailsService) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 方法名: configure <br>
     * 方法描述: 重写配置方法 <br>
     *
     * @param http 请求
     * @date 创建时间: 2021/9/19 21:06 <br>
     * @author zhangyu
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthEntryPoint())//没有权限访问
                .and().csrf().disable()   //关闭csrf防护
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/admin/acl/index/logout")//退出路径
                .addLogoutHandler(new TokenLogOutHandler(tokenManager, redisTemplate)).and()    //指定自定义退出处理器
                .addFilter(new TokenLoginFilter(tokenManager, redisTemplate, authenticationManager()))  //指定自定义认证过滤器
                .addFilter(new TokenAuthFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();
        //指定自定义授权过滤器
    }

    /**
     * 方法名: configure <br>
     * 方法描述: 指定查询用户名和密码使用的userDetailsService和PasswordEncoder <br>
     *
     * @param auth 权限
     * @date 创建时间: 2021/9/19 21:12 <br>
     * @author zhangyu
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 方法名: configure <br>
     * 方法描述: 设置不需要认证和授权就能访问的路径 <br>
     *
     * @param web 权限
     * @date 创建时间: 2021/9/19 21:14 <br>
     * @author zhangyu
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**");
    }
}
