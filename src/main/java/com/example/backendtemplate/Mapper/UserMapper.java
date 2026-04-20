package com.example.backendtemplate.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backendtemplate.Entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
