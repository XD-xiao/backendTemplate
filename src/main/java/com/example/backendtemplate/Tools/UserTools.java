package com.example.backendtemplate.Tools;

import com.example.backendtemplate.Entity.Query.UserQuery;
import com.example.backendtemplate.Entity.User;
import com.example.backendtemplate.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserTools {

    private final UserService userService;


    @Tool(description = "查询人物")
    public List<User> queryUser(@ToolParam(description = "人物名称")UserQuery query){


        List<User> users = userService.queryUsers(query).getRecords();
        System.out.println(users);

        return users;
    }

}
