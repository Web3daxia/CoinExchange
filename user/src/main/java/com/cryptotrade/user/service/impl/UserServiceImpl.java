/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service.impl;

import com.cryptotrade.util.JwtUtil;
import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.LoginResponse;
import com.cryptotrade.user.dto.response.RegisterResponse;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.entity.UserRegistrationKey;
import com.cryptotrade.user.repository.UserRepository;
import com.cryptotrade.user.service.RegistrationKeyService;
import com.cryptotrade.user.service.UserService;
import com.cryptotrade.user.service.VerificationService;
import com.cryptotrade.user.util.TOTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TOTPUtil totpUtil;
    
    @Autowired
    private RegistrationKeyService registrationKeyService;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String registerType = request.getRegisterType();
        if ("PHONE".equals(registerType)) {
            PhoneRegisterRequest phoneRequest = new PhoneRegisterRequest();
            phoneRequest.setPhone(request.getPhone());
            phoneRequest.setCountryCode(request.getCountryCode());
            phoneRequest.setVerificationCode(request.getVerificationCode());
            phoneRequest.setPassword(request.getPassword());
            return registerByPhone(phoneRequest);
        } else if ("EMAIL".equals(registerType)) {
            EmailRegisterRequest emailRequest = new EmailRegisterRequest();
            emailRequest.setEmail(request.getEmail());
            emailRequest.setVerificationCode(request.getVerificationCode());
            emailRequest.setPassword(request.getPassword());
            return registerByEmail(emailRequest);
        } else {
            throw new IllegalArgumentException("不支持的注册类型: " + registerType + "，仅支持PHONE和EMAIL");
        }
    }

    @Override
    @Transactional
    public RegisterResponse registerByPhone(PhoneRegisterRequest request) {
        String phone = request.getPhone();
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("该手机号已被注册");
        }

        // 验证验证码
        if (!verificationService.verifyCode(phone, request.getVerificationCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        User user = new User();
        user.setPhone(phone);
        user.setCountryCode(request.getCountryCode());
        user.setUsername(generateUsername(phone));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountStatus("ACTIVE");
        user.setKycLevel(0);
        user.setKycStatus("PENDING");
        user.setTwoFaEnabled(false);

        user = userRepository.save(user);
        
        // 生成注册私钥
        UserRegistrationKey registrationKey = registrationKeyService.generateRegistrationKey(user.getId());
        
        // 构建响应
        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRegistrationKey(registrationKey.getRegistrationKey());
        
        return response;
    }

    @Override
    @Transactional
    public RegisterResponse registerByEmail(EmailRegisterRequest request) {
        String email = request.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("该邮箱已被注册");
        }

        // 验证验证码
        if (!verificationService.verifyCode(email, request.getVerificationCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(generateUsername(email));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountStatus("ACTIVE");
        user.setKycLevel(0);
        user.setKycStatus("PENDING");
        user.setTwoFaEnabled(false);

        user = userRepository.save(user);
        
        // 生成注册私钥
        UserRegistrationKey registrationKey = registrationKeyService.generateRegistrationKey(user.getId());
        
        // 构建响应
        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRegistrationKey(registrationKey.getRegistrationKey());
        
        return response;
    }

    @Override
    @Transactional
    public void registerByPrivateKey(PrivateKeyRegisterRequest request) {
        String privateKey = request.getPrivateKey();
        if (privateKey == null || privateKey.isEmpty()) {
            throw new RuntimeException("私钥不能为空");
        }

        // 对私钥进行哈希存储
        String privateKeyHash = hashPrivateKey(privateKey);

        // 检查私钥是否已被注册
        if (userRepository.findByPrivateKeyHash(privateKeyHash).isPresent()) {
            throw new RuntimeException("该私钥已被注册");
        }

        User user = new User();
        user.setUsername("user_" + System.currentTimeMillis());
        user.setPrivateKeyHash(privateKeyHash);
        user.setAccountStatus("ACTIVE");
        user.setKycLevel(0);
        user.setKycStatus("PENDING");
        user.setTwoFaEnabled(false);

        userRepository.save(user);
        
        // 生成注册私钥
        registrationKeyService.generateRegistrationKey(user.getId());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String loginType = request.getLoginType();
        if ("PASSWORD".equals(loginType)) {
            return loginByPassword(request);
        } else if ("REGISTRATION_KEY".equals(loginType)) {
            return loginByRegistrationKey(request.getPrivateKey());
        } else {
            throw new IllegalArgumentException("不支持的登录类型: " + loginType);
        }
    }

    @Override
    public LoginResponse loginByPassword(LoginRequest request) {
        String account = request.getAccount();
        User user = userRepository.findByEmail(account)
                .orElse(userRepository.findByPhone(account).orElse(null));

        if (user == null) {
            throw new RuntimeException("登录失败，用户名或密码错误");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("登录失败，用户名或密码错误");
        }

        // 检查账户状态
        if (!"ACTIVE".equals(user.getAccountStatus())) {
            throw new RuntimeException("账户已被冻结或禁用");
        }

        // 如果启用了2FA，需要验证2FA码
        if (Boolean.TRUE.equals(user.getTwoFaEnabled()) && user.getTwoFaSecret() != null) {
            String twoFactorCode = request.getTwoFactorCode();
            if (twoFactorCode == null || !totpUtil.verifyCode(user.getTwoFaSecret(), twoFactorCode)) {
                throw new RuntimeException("2FA验证码错误或未提供");
            }
        }

        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse loginByPrivateKey(String privateKey) {
        // 保留此方法以兼容旧接口，实际使用loginByRegistrationKey
        return loginByRegistrationKey(privateKey);
    }
    
    /**
     * 使用注册私钥登录
     */
    public LoginResponse loginByRegistrationKey(String registrationKey) {
        if (registrationKey == null || registrationKey.isEmpty()) {
            throw new RuntimeException("注册私钥不能为空");
        }

        Long userId = registrationKeyService.getUserIdByRegistrationKey(registrationKey);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查账户状态
        if (!"ACTIVE".equals(user.getAccountStatus())) {
            throw new RuntimeException("账户已被冻结或禁用");
        }

        boolean needBindContact = (user.getEmail() == null && user.getPhone() == null);
        LoginResponse response = buildLoginResponse(user);
        response.setNeedBindContact(needBindContact);

        return response;
    }

    @Override
    @Transactional
    public LoginResponse loginByOAuth(OAuthLoginRequest request) {
        // TODO: 实现OAuth登录逻辑
        // 这里需要根据不同的provider（GOOGLE, FACEBOOK等）调用相应的OAuth API
        // 获取用户信息后，查找或创建用户账户
        
        String provider = request.getProvider();
        String oauthToken = request.getOauthToken();
        
        // 示例：模拟OAuth登录
        // 实际应该调用OAuth提供商的API验证token并获取用户信息
        throw new RuntimeException("OAuth登录功能暂未实现，需要集成OAuth SDK");
    }

    @Override
    @Transactional
    public void bindEmail(Long userId, BindEmailRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String email = request.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("该邮箱已被其他用户使用");
        }

        // 验证验证码
        if (!verificationService.verifyCode(email, request.getVerificationCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void bindPhone(Long userId, BindPhoneRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String phone = request.getPhone();
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("该手机号已被其他用户使用");
        }

        // 验证验证码
        if (!verificationService.verifyCode(phone, request.getVerificationCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        user.setPhone(phone);
        user.setCountryCode(request.getCountryCode());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        String resetType = request.getResetType();
        String contact = "EMAIL".equals(resetType) ? request.getEmail() : request.getPhone();

        // 验证验证码
        if (!verificationService.verifyCode(contact, request.getVerificationCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        User user;
        if ("EMAIL".equals(resetType)) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } else {
            user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        }

        // 私钥注册的用户必须已绑定邮箱或手机号
        if (user.getPassword() == null && user.getEmail() == null && user.getPhone() == null) {
            throw new RuntimeException("私钥注册的用户必须先绑定邮箱或手机号才能重置密码");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String enable2FA(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (Boolean.TRUE.equals(user.getTwoFaEnabled())) {
            throw new RuntimeException("2FA已启用");
        }

        String secret = totpUtil.generateSecret();
        user.setTwoFaSecret(secret);
        user.setTwoFaEnabled(true);
        userRepository.save(user);

        return secret;
    }

    @Override
    public boolean verify2FA(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!Boolean.TRUE.equals(user.getTwoFaEnabled()) || user.getTwoFaSecret() == null) {
            throw new RuntimeException("2FA未启用");
        }

        return totpUtil.verifyCode(user.getTwoFaSecret(), code);
    }

    @Override
    @Transactional
    public void disable2FA(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!Boolean.TRUE.equals(user.getTwoFaEnabled())) {
            throw new RuntimeException("2FA未启用");
        }

        // 验证密码
        if (user.getPassword() != null && !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        user.setTwoFaEnabled(false);
        user.setTwoFaSecret(null);
        userRepository.save(user);
    }

    private LoginResponse buildLoginResponse(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Long expiresIn = 86400L; // 24小时

        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setExpiresIn(expiresIn);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNeedBindContact(false);

        return response;
    }

    private String generateUsername(String identifier) {
        String base = identifier.replaceAll("[^a-zA-Z0-9]", "");
        String username = base;
        int suffix = 1;
        while (userRepository.existsByUsername(username)) {
            username = base + suffix++;
        }
        return username;
    }

    private String hashPrivateKey(String privateKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(privateKey.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("私钥哈希失败", e);
        }
    }
}
