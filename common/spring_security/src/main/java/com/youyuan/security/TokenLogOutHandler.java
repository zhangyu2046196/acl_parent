package com.youyuan.security;

import cn.hutool.core.util.StrUtil;
import com.youyuan.utils.R;
import com.youyuan.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名称：TokenLogOutHandler <br>
 * 类描述： 退出操作删除token工具 <br>
 *
 * @author zhangyu
 * @version 1.0.0
 * @date 创建时间：2021/9/19 16:07<br>
 */
@Component
public class TokenLogOutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogOutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 方法名: logout <br>
     * 方法描述: 退出操作接口 <br>
     *
     * @param request
     * @param response
     * @param authentication
     * @date 创建时间: 2021/9/19 16:08 <br>
     * @author zhangyu
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1.从request请求头中获取token
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            //2.删除token
            tokenManager.removeToken(token);
            //3.通过token获取用户名
            String userName = tokenManager.getUserInfoByToken(token);
            //4.从redis删除用户名key
            redisTemplate.delete(userName);
        }
        //5.返回信息写入输出流
        ResponseUtil.out(response, R.ok());
    }
}
