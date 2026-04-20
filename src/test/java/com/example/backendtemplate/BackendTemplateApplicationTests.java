package com.example.backendtemplate;

import com.example.backendtemplate.Entity.Query.UserQuery;
import com.example.backendtemplate.Entity.User;
import com.example.backendtemplate.Mapper.UserMapper;
import com.example.backendtemplate.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendTemplateApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {

        UserQuery query = new UserQuery();
        query.setName("爱因斯坦");

        List<User> users = userService.queryUsers(query).getRecords();
        System.out.println(users);
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelect() {
        System.out.println(userMapper.selectList(null)); // 直接调用 Mapper
    }

}
