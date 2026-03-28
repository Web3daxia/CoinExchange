/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.schedule;

import com.cryptotrade.basicinfo.service.ApiEndpointService;
import com.cryptotrade.basicinfo.service.ExchangeRateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * 基础信息定时任务
 */
@Component
public class BasicInfoScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(BasicInfoScheduleTask.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ApiEndpointService apiEndpointService;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 币安API基础地址
    private static final String BINANCE_API_BASE = "https://api.binance.com/api/v3";
    
    // CoinGecko API基础地址（用于获取法币汇率）
    private static final String COINGECKO_API_BASE = "https://api.coingecko.com/api/v3";

    // 主要支持的加密货币列表（对USDT）
    private static final List<String> CRYPTO_CURRENCIES = Arrays.asList(
            "BTC", "ETH", "BNB", "XRP", "ADA", "SOL", "DOT", "DOGE", "MATIC", "LTC", "AVAX", "LINK", "UNI", "ATOM"
    );

    // 主要支持的法币列表（USDT对法币）
    private static final List<String> FIAT_CURRENCIES = Arrays.asList(
            "USD", "CNY", "EUR", "GBP", "JPY", "KRW", "SGD", "HKD"
    );

    /**
     * 更新汇率
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    public void updateExchangeRates() {
        if (restTemplate == null) {
            logger.warn("RestTemplate未配置，跳过汇率更新");
            return;
        }

        logger.info("开始更新汇率...");
        int successCount = 0;
        int failCount = 0;

        try {
            // 1. 更新加密货币对USDT的汇率（从币安获取）
            for (String crypto : CRYPTO_CURRENCIES) {
                try {
                    BigDecimal rate = getCryptoToUsdtRate(crypto);
                    if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
                        exchangeRateService.updateExchangeRate(crypto, "USDT", rate, "BINANCE");
                        successCount++;
                        logger.debug("更新汇率成功: {} -> USDT = {}", crypto, rate);
                    }
                } catch (Exception e) {
                    failCount++;
                    logger.warn("更新 {} -> USDT 汇率失败: {}", crypto, e.getMessage());
                }
            }

            // 2. 更新USDT对法币的汇率（从CoinGecko获取）
            for (String fiat : FIAT_CURRENCIES) {
                try {
                    BigDecimal rate = getUsdtToFiatRate(fiat);
                    if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
                        exchangeRateService.updateExchangeRate("USDT", fiat, rate, "COINGECKO");
                        successCount++;
                        logger.debug("更新汇率成功: USDT -> {} = {}", fiat, rate);
                    }
                } catch (Exception e) {
                    failCount++;
                    logger.warn("更新 USDT -> {} 汇率失败: {}", fiat, e.getMessage());
                }
            }

            logger.info("汇率更新完成: 成功 {} 条, 失败 {} 条", successCount, failCount);
        } catch (Exception e) {
            logger.error("更新汇率时发生异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 从币安API获取加密货币对USDT的汇率
     */
    private BigDecimal getCryptoToUsdtRate(String crypto) {
        try {
            // 币安API: GET /api/v3/ticker/price?symbol=BTCUSDT
            String symbol = crypto + "USDT";
            String url = BINANCE_API_BASE + "/ticker/price?symbol=" + symbol;
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("price")) {
                String priceStr = jsonNode.get("price").asText();
                return new BigDecimal(priceStr).setScale(8, RoundingMode.HALF_UP);
            }
            
            return null;
        } catch (Exception e) {
            logger.error("从币安获取 {} -> USDT 汇率失败: {}", crypto, e.getMessage());
            return null;
        }
    }

    /**
     * 从CoinGecko API获取USDT对法币的汇率
     */
    private BigDecimal getUsdtToFiatRate(String fiat) {
        try {
            // CoinGecko API: GET /api/v3/simple/price?ids=tether&vs_currencies=usd,cny,eur
            // 注意：CoinGecko返回的是1 USDT = X 法币，所以直接使用
            String url = COINGECKO_API_BASE + "/simple/price?ids=tether&vs_currencies=" + fiat.toLowerCase();
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("tether") && jsonNode.get("tether").has(fiat.toLowerCase())) {
                String rateStr = jsonNode.get("tether").get(fiat.toLowerCase()).asText();
                return new BigDecimal(rateStr).setScale(8, RoundingMode.HALF_UP);
            }
            
            return null;
        } catch (Exception e) {
            logger.error("从CoinGecko获取 USDT -> {} 汇率失败: {}", fiat, e.getMessage());
            return null;
        }
    }

    /**
     * 检查API端点健康状态
     * 每10分钟执行一次
     */
    @Scheduled(fixedRate = 600000)
    public void checkApiEndpointHealth() {
        // TODO: 检查所有API端点的健康状态
        // List<ApiEndpoint> endpoints = apiEndpointRepository.findByStatus("ACTIVE");
        // for (ApiEndpoint endpoint : endpoints) {
        //     apiEndpointService.checkEndpointHealth(endpoint.getId());
        // }
    }
}














