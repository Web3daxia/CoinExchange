/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.CurrencyName;
import com.cryptotrade.systemmanagement.repository.CurrencyNameRepository;
import com.cryptotrade.systemmanagement.service.CurrencyNameService;
import com.cryptotrade.systemmanagement.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 币种多语言名称Service实现
 * 注意：这里提供基础的翻译逻辑框架，实际生产环境应该集成第三方翻译API
 */
@Service
public class CurrencyNameServiceImpl implements CurrencyNameService {

    @Autowired
    private CurrencyNameRepository currencyNameRepository;

    @Override
    @Transactional
    public void createCurrencyNames(Long currencyId, String defaultName, String sourceLanguage) {
        // 删除已存在的名称
        currencyNameRepository.deleteByCurrencyId(currencyId);

        // 为每种语言创建名称
        for (String languageCode : LanguageUtil.SUPPORTED_LANGUAGES) {
            CurrencyName currencyName = new CurrencyName();
            currencyName.setCurrencyId(currencyId);
            currencyName.setLanguageCode(languageCode);
            
            // 如果源语言和目标语言相同，直接使用
            if (languageCode.equals(sourceLanguage)) {
                currencyName.setCurrencyName(defaultName);
            } else {
                // 否则进行翻译（这里使用简单的占位符，实际应该调用翻译API）
                currencyName.setCurrencyName(translateCurrencyName(defaultName, sourceLanguage, languageCode));
            }
            
            currencyNameRepository.save(currencyName);
        }
    }

    @Override
    @Transactional
    public void updateCurrencyNames(Long currencyId, String newName, String sourceLanguage) {
        createCurrencyNames(currencyId, newName, sourceLanguage);
    }

    @Override
    public List<CurrencyName> getCurrencyNames(Long currencyId) {
        return currencyNameRepository.findByCurrencyId(currencyId);
    }

    @Override
    public String getCurrencyName(Long currencyId, String languageCode) {
        Optional<CurrencyName> currencyName = currencyNameRepository.findByCurrencyIdAndLanguageCode(currencyId, languageCode);
        return currencyName.map(CurrencyName::getCurrencyName).orElse(null);
    }

    @Override
    public Map<String, String> getCurrencyNameMap(Long currencyId) {
        List<CurrencyName> names = getCurrencyNames(currencyId);
        Map<String, String> nameMap = new HashMap<>();
        for (CurrencyName name : names) {
            nameMap.put(name.getLanguageCode(), name.getCurrencyName());
        }
        return nameMap;
    }

    @Override
    @Transactional
    public void deleteCurrencyNames(Long currencyId) {
        currencyNameRepository.deleteByCurrencyId(currencyId);
    }

    /**
     * 翻译币种名称
     * 注意：这是一个占位符方法，实际应该调用第三方翻译API（如Google Translate、百度翻译等）
     * 
     * @param text 要翻译的文本
     * @param sourceLanguage 源语言代码
     * @param targetLanguage 目标语言代码
     * @return 翻译后的文本
     */
    private String translateCurrencyName(String text, String sourceLanguage, String targetLanguage) {
        // TODO: 集成第三方翻译API
        // 示例：可以使用Google Cloud Translation API、百度翻译API、有道翻译API等
        
        // 临时实现：如果源语言是英文，返回英文；否则返回文本+"("+语言代码+")"
        if ("en-US".equals(sourceLanguage)) {
            // 如果是英文，默认其他语言也使用英文（实际应该翻译）
            return text;
        } else {
            // 临时处理：返回原文（实际应该调用翻译API）
            return text;
        }
        
        // 实际实现示例（伪代码）：
        // return translationService.translate(text, sourceLanguage, targetLanguage);
    }
}














