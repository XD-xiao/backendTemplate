package com.example.backendtemplate.Pojo;

import lombok.Data;

@Data
public class TestObject {
    private Long testId;
    private String name;
    private String email;

    public TestObject() {
    }

    public TestObject(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public TestObject(Long testId, String name, String email) {
        this.testId = testId;
        this.name = name;
        this.email = email;
    }
}
