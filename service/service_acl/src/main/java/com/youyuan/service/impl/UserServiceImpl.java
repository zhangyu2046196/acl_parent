package com.youyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youyuan.entity.AclUser;
import com.youyuan.mapper.UserMapper;
import com.youyuan.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, AclUser> implements UserService {

    @Override
    public AclUser selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<AclUser>().eq("username", username));
    }
}
