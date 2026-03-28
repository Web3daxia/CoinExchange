/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.entity.LanguagePack;
import com.cryptotrade.basicinfo.entity.UserPreference;
import com.cryptotrade.basicinfo.repository.LanguagePackRepository;
import com.cryptotrade.basicinfo.repository.UserPreferenceRepository;
import com.cryptotrade.basicinfo.service.GeoIPService;
import com.cryptotrade.basicinfo.service.LanguageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 语言服务实现类
 */
@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguagePackRepository languagePackRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private GeoIPService geoIPService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // IP地址与语言的映射（简化处理，实际应该使用GeoIP服务）
    private static final Map<String, String> IP_TO_LANGUAGE = new HashMap<>();
    static {
        IP_TO_LANGUAGE.put("CN", "zh-CN");
        IP_TO_LANGUAGE.put("US", "en");
        IP_TO_LANGUAGE.put("GB", "en");
        IP_TO_LANGUAGE.put("FR", "fr");
        IP_TO_LANGUAGE.put("ES", "es");
        IP_TO_LANGUAGE.put("DE", "de");
        IP_TO_LANGUAGE.put("JP", "ja");
        IP_TO_LANGUAGE.put("KR", "ko");
        IP_TO_LANGUAGE.put("SA", "ar");
    }

    @Override
    public List<LanguagePack> getSupportedLanguages() {
        return languagePackRepository.findByStatus("ACTIVE");
    }

    @Override
    public String detectLanguageByIp(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            // IP地址为空，返回默认语言
            return getDefaultLanguage();
        }

        try {
            // 使用GeoIP服务根据IP地址获取国家代码
            String countryCode = geoIPService.getCountryCode(ipAddress);
            
            if (countryCode != null && !countryCode.isEmpty()) {
                // 根据国家代码映射到语言代码
                String languageCode = IP_TO_LANGUAGE.get(countryCode);
                if (languageCode != null) {
                    // 验证语言包是否存在且激活
                    Optional<LanguagePack> languagePack = languagePackRepository.findByLanguageCode(languageCode);
                    if (languagePack.isPresent() && "ACTIVE".equals(languagePack.get().getStatus())) {
                        return languageCode;
                    }
                }
            }
        } catch (Exception e) {
            // GeoIP服务调用失败，记录日志但不抛出异常，返回默认语言
            org.slf4j.LoggerFactory.getLogger(LanguageServiceImpl.class)
                    .warn("根据IP地址 {} 检测语言失败: {}", ipAddress, e.getMessage());
        }

        // 如果无法检测或映射失败，返回默认语言
        return getDefaultLanguage();
    }

    /**
     * 获取默认语言
     */
    private String getDefaultLanguage() {
        Optional<LanguagePack> defaultLang = languagePackRepository.findByIsDefaultTrueAndStatus("ACTIVE");
        return defaultLang.map(LanguagePack::getLanguageCode).orElse("en");
    }

    @Override
    public Map<String, String> getTranslationData(String languageCode) {
        Optional<LanguagePack> languagePack = languagePackRepository.findByLanguageCode(languageCode);
        if (languagePack.isPresent() && languagePack.get().getTranslationData() != null) {
            try {
                return objectMapper.readValue(
                        languagePack.get().getTranslationData(),
                        new TypeReference<Map<String, String>>() {});
            } catch (Exception e) {
                return new HashMap<>();
            }
        }
        return new HashMap<>();
    }

    @Override
    @Transactional
    public UserPreference setUserLanguage(Long userId, String languageCode) {
        Optional<UserPreference> existing = userPreferenceRepository.findByUserId(userId);
        
        UserPreference preference;
        if (existing.isPresent()) {
            preference = existing.get();
            preference.setLanguageCode(languageCode);
        } else {
            preference = new UserPreference();
            preference.setUserId(userId);
            preference.setLanguageCode(languageCode);
            preference.setCurrency("USD"); // 默认USD
        }

        return userPreferenceRepository.save(preference);
    }

    @Override
    public UserPreference getUserPreference(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // 返回默认偏好
                    UserPreference defaultPref = new UserPreference();
                    defaultPref.setUserId(userId);
                    defaultPref.setLanguageCode("en");
                    defaultPref.setCurrency("USD");
                    return defaultPref;
                });
    }
}














