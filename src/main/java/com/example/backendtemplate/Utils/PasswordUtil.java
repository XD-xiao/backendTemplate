package com.example.backendtemplate.Utils;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码加密与校验工具类（使用 Hutool 的 BCrypt 实现）
 */
public final class PasswordUtil {

    // 私有构造函数，防止外部实例化
    private PasswordUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 对原始密码进行加密
     * @param rawPassword 明文密码
     * @return 加密后的密文
     */
    public static String encode(String rawPassword) {
        // gensalt() 生成盐，hashpw() 进行哈希加密
        // 默认强度为 10 (对应 BCrypt.VERSION = '$2a')
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 验证原始密码与加密后的密码是否匹配
     * @param rawPassword 明文密码
     * @param encodedPassword 数据库中存储的密文
     * @return 匹配返回 true，否则返回 false
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}