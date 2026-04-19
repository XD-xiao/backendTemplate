package com.example.backendtemplate.Exception;

import com.example.backendtemplate.Pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("请求路径不存在: {}", ex.getRequestURL());
        return Result.error(404,"请求路径不存在，请检查URL后重试");
    }

    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointerException(NullPointerException ex) {
        log.error("空指针异常: ", ex);
        return Result.error(500,"系统内部错误，请联系管理员");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("非法参数: {}", ex.getMessage());
        return Result.error(400,"参数错误：" + ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("静态资源未找到: {}", ex.getResourcePath());
        return Result.error(404,"请求的资源不存在，请检查路径");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        String message = "数据冲突：请检查输入内容是否重复";

        // 尝试提取更友好的提示（注意：不要暴露数据库细节）
        if (ex.getCause() != null && ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
            // 可根据具体约束名进一步细化，但需谨慎
            message = "该邮箱或用户名已被注册";
        }

        log.warn("数据库唯一键冲突: {}", ex.getMessage());
        return Result.error(400,message);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        log.warn("不支持的媒体类型请求: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Unsupported media type");
    }

    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handleTokenInvalid(TokenInvalidException ex) {
        log.warn("无效或过期的 Token: {}", ex.getMessage());
        return Result.error(401,ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public Result handleGenericException(Exception e) {
        log.error("未预期的服务器异常: ", e); // 记录完整堆栈
        return Result.error(500,"服务器内部错误，请稍后再试");
    }
}