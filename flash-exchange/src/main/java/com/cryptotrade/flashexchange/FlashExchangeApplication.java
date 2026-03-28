/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 闪兑交易模块主应用类
 */
@SpringBootApplication(scanBasePackages = {
    "com.cryptotrade.flashexchange",
    "com.cryptotrade.common",
    "com.cryptotrade.user",
    "com.cryptotrade.wallet",
    "com.cryptotrade.spot"
})
@EnableJpaAuditing
@EnableScheduling
public class FlashExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlashExchangeApplication.class, args);
    }
}












