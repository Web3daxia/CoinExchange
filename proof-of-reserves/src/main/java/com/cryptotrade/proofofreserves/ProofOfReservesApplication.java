/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 储备金证明模块主应用类
 */
@SpringBootApplication(scanBasePackages = {
    "com.cryptotrade.proofofreserves",
    "com.cryptotrade.common",
    "com.cryptotrade.user",
    "com.cryptotrade.wallet"
})
@EnableJpaAuditing
@EnableScheduling
public class ProofOfReservesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProofOfReservesApplication.class, args);
    }
}












