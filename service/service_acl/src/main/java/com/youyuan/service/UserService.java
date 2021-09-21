package com.youyuan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.youyuan.entity.AclUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface UserService extends IService<AclUser> {

    // 从数据库中取出用户信息
    AclUser selectByUsername(String username);
}
