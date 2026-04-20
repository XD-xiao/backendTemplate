package com.example.backendtemplate.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.backendtemplate.Entity.Query.UserQuery;
import com.example.backendtemplate.Entity.User;

import java.util.List;

public interface UserService {
    IPage<User> queryUsers(UserQuery query);

}
