package com.example.backendtemplate.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backendtemplate.Entity.Query.UserQuery;
import com.example.backendtemplate.Entity.User;
import com.example.backendtemplate.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backendtemplate.Mapper.UserMapper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

     @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<User> queryUsers(UserQuery query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like("name", query.getName());
        }
        if (query.getDescription() != null && !query.getDescription().isEmpty()) {
            wrapper.like("description", query.getDescription());
        }

        return userMapper.selectPage(page, wrapper);
    }

}
