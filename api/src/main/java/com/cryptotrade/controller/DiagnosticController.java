/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.controller;

import com.cryptotrade.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 诊断控制器 - 用于检查所有已注册的路径映射
 */
@RestController
@RequestMapping("/diagnostic")
public class DiagnosticController {

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @GetMapping("/mappings")
    public Result<Map<String, Object>> getAllMappings() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有已注册的路径映射（包含方法和路径）
        java.util.List<Map<String, String>> mappingDetails = handlerMapping.getHandlerMethods().entrySet().stream()
            .map(entry -> {
                Map<String, String> detail = new HashMap<>();
                RequestMappingInfo mapping = entry.getKey();
                
                // 获取路径
                String path = "";
                org.springframework.web.servlet.mvc.condition.PatternsRequestCondition patternsCondition = 
                    mapping.getPatternsCondition();
                if (patternsCondition != null) {
                    path = patternsCondition.getPatterns().stream()
                        .findFirst()
                        .orElse("");
                }
                
                // 获取HTTP方法
                String methods = "";
                if (mapping.getMethodsCondition() != null && !mapping.getMethodsCondition().getMethods().isEmpty()) {
                    methods = mapping.getMethodsCondition().getMethods().stream()
                        .map(method -> method.toString())
                        .collect(Collectors.joining(", "));
                } else {
                    methods = "ALL"; // 如果没有指定方法，表示支持所有方法
                }
                
                // 获取控制器类名
                String controllerClass = entry.getValue().getBeanType().getSimpleName();
                String controllerFullName = entry.getValue().getBeanType().getName();
                
                detail.put("method", methods);
                detail.put("path", path);
                detail.put("controller", controllerClass);
                detail.put("controllerFullName", controllerFullName);
                
                return detail;
            })
            .sorted((a, b) -> {
                // 按路径排序
                int pathCompare = a.get("path").compareTo(b.get("path"));
                if (pathCompare != 0) return pathCompare;
                // 路径相同则按方法排序
                return a.get("method").compareTo(b.get("method"));
            })
            .collect(Collectors.toList());
        
        result.put("totalMappings", mappingDetails.size());
        result.put("mappings", mappingDetails);
        
        // 检查 AdminAuthController 是否存在
        boolean hasAdminAuthController = mappingDetails.stream()
            .anyMatch(m -> m.get("controllerFullName").contains("AdminAuthController"));
        
        // 检查 /admin/login 路径是否存在
        boolean hasAdminLogin = mappingDetails.stream()
            .anyMatch(m -> {
                String path = m.get("path");
                String method = m.get("method");
                return path.contains("/admin/login") && (method.contains("POST") || method.equals("ALL"));
            });
        
        result.put("hasAdminAuthController", hasAdminAuthController);
        result.put("hasAdminLogin", hasAdminLogin);
        
        // 获取所有包含 admin 或 login 的路径
        result.put("adminLoginPaths", mappingDetails.stream()
            .filter(m -> {
                String path = m.get("path");
                return path.contains("admin") || path.contains("login");
            })
            .collect(Collectors.toList()));
        
        return Result.success(result);
    }

    @GetMapping("/controllers")
    public Result<Map<String, Object>> getControllers() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有控制器（包含详细信息）
        java.util.List<Map<String, String>> controllerDetails = handlerMapping.getHandlerMethods().values().stream()
            .map(handler -> {
                Map<String, String> detail = new HashMap<>();
                String fullName = handler.getBeanType().getName();
                String simpleName = handler.getBeanType().getSimpleName();
                String packageName = handler.getBeanType().getPackage() != null ? 
                    handler.getBeanType().getPackage().getName() : "";
                
                detail.put("fullName", fullName);
                detail.put("simpleName", simpleName);
                detail.put("packageName", packageName);
                
                return detail;
            })
            .collect(Collectors.toList());
        
        // 去重（基于全限定名）
        Set<String> uniqueControllers = controllerDetails.stream()
            .map(d -> d.get("fullName"))
            .collect(Collectors.toSet());
        
        result.put("totalControllers", uniqueControllers.size());
        result.put("controllers", controllerDetails);
        
        // 检查是否有 AdminAuthController
        boolean hasAdminAuthController = controllerDetails.stream()
            .anyMatch(d -> d.get("fullName").contains("AdminAuthController"));
        
        // 检查是否有 system-management 模块的控制器
        boolean hasSystemManagementControllers = controllerDetails.stream()
            .anyMatch(d -> d.get("packageName").contains("systemmanagement"));
        
        // 获取所有 system-management 相关的控制器
        java.util.List<String> systemManagementControllers = controllerDetails.stream()
            .filter(d -> d.get("packageName").contains("systemmanagement"))
            .map(d -> d.get("fullName"))
            .distinct()
            .collect(Collectors.toList());
        
        result.put("hasAdminAuthController", hasAdminAuthController);
        result.put("hasSystemManagementControllers", hasSystemManagementControllers);
        result.put("systemManagementControllers", systemManagementControllers);
        
        return Result.success(result);
    }
    
    /**
     * 测试密码验证
     * 用于诊断登录问题
     * 使用方式: GET /api/diagnostic/test-password?password=123456
     */
    @GetMapping("/test-password")
    public Result<Map<String, Object>> testPassword(@RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 使用 RequestContextHolder 获取 ServletContext
            javax.servlet.http.HttpServletRequest request = 
                (javax.servlet.http.HttpServletRequest) 
                    org.springframework.web.context.request.RequestContextHolder
                        .currentRequestAttributes();
            
            javax.servlet.ServletContext servletContext = request.getServletContext();
            
            if (servletContext == null) {
                result.put("error", "无法获取 ServletContext");
                return Result.error("无法获取 ServletContext");
            }
            
            // 获取 ApplicationContext
            org.springframework.context.ApplicationContext context = 
                org.springframework.web.context.support.WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            
            if (context == null) {
                result.put("error", "无法获取 ApplicationContext");
                return Result.error("无法获取 ApplicationContext");
            }
            
            // 获取 SystemAdminRepository
            Object adminRepo = context.getBean("systemAdminRepository");
            java.lang.reflect.Method findByUsername = adminRepo.getClass()
                .getMethod("findByUsername", String.class);
            java.util.Optional<?> adminOpt = (java.util.Optional<?>) findByUsername.invoke(adminRepo, "admin");
            
            if (!adminOpt.isPresent()) {
                result.put("error", "admin用户不存在");
                return Result.error("admin用户不存在");
            }
            
            Object admin = adminOpt.get();
            java.lang.reflect.Method getPassword = admin.getClass().getMethod("getPassword");
            String dbPassword = (String) getPassword.invoke(admin);
            
            // 获取 PasswordEncoder
            org.springframework.security.crypto.password.PasswordEncoder encoder = 
                context.getBean(org.springframework.security.crypto.password.PasswordEncoder.class);
            
            boolean matches = encoder.matches(password, dbPassword);
            
            result.put("inputPassword", password);
            result.put("dbPasswordHash", dbPassword);
            result.put("hashLength", dbPassword != null ? dbPassword.length() : 0);
            result.put("hashPrefix", dbPassword != null && dbPassword.length() > 20 ? 
                dbPassword.substring(0, 20) + "..." : dbPassword);
            result.put("matches", matches);
            result.put("message", matches ? "✓ 密码匹配成功" : "✗ 密码不匹配");
            result.put("encoderClass", encoder.getClass().getName());
            result.put("encoderHashCode", encoder.hashCode());
            
            return Result.success(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("errorClass", e.getClass().getName());
            if (e.getCause() != null) {
                result.put("cause", e.getCause().getMessage());
            }
            return Result.error("测试失败: " + e.getMessage());
        }
    }
}

