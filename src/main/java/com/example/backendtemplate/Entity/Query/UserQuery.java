package com.example.backendtemplate.Entity.Query;

import lombok.Data;

@Data
public class UserQuery {
    private String name;
    private String description;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
