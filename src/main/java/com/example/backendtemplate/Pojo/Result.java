package com.example.backendtemplate.Pojo;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;       // 状态码
    private String message;     // 提示信息
    private T data;            // 业务数据（泛型，可以是任何类型）
    private Long timestamp;    // 时间戳

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}