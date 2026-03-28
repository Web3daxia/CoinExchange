/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 * 用于生成BCrypt加密密码
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成密码 "123456" 的BCrypt哈希
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("==========================================");
        System.out.println("密码加密工具");
        System.out.println("==========================================");
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt加密后的密码: " + encodedPassword);
        System.out.println();
        System.out.println("验证密码是否正确: " + encoder.matches(password, encodedPassword));
        System.out.println();
        System.out.println("可以直接用于数据库的SQL语句:");
        System.out.println("UPDATE system_admins SET password = '" + encodedPassword + "' WHERE username = 'admin';");
        System.out.println("==========================================");
    }
}


