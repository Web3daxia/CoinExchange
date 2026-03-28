/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service;

import com.cryptotrade.spotbot.dto.request.MatchOrderRequest;
import com.cryptotrade.spotbot.dto.response.MatchOrderResponse;

public interface OrderMatchingService {
    /**
     * 撮合用户订单
     * @param request 订单请求
     * @return 撮合结果
     */
    MatchOrderResponse matchUserOrder(MatchOrderRequest request);
    
    /**
     * 生成机器人订单（无用户订单时）
     * @param pairName 交易对名称
     * @return 是否成功
     */
    boolean generateBotOrder(String pairName);
    
    /**
     * 启动机器人
     * @param pairName 交易对名称
     */
    void startBot(String pairName);
    
    /**
     * 停止机器人
     * @param pairName 交易对名称
     */
    void stopBot(String pairName);
}














