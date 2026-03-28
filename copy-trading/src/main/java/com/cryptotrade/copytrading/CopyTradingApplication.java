/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 跟单模块主应用类
 */
@SpringBootApplication(scanBasePackages = {
    "com.cryptotrade.copytrading",
    "com.cryptotrade.common",
    "com.cryptotrade.wallet",
    "com.cryptotrade.user",
    "com.cryptotrade.spot",
    "com.cryptotrade.futures.usdt",
    "com.cryptotrade.futures.coin"
})
@EnableJpaAuditing
@EnableScheduling
public class CopyTradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CopyTradingApplication.class, args);
    }
}












