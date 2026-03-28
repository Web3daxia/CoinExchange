/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * U本位永续合约委托搜索请求DTO
 */
@Data
public class UsdtFuturesOrderSearchRequest {
    private String email; // 会员邮箱
    private Long userId; // 用户ID
    private String memberUid; // 会员UID
    private String phone; // 手机号
    private LocalDateTime startTime; // 时间开始
    private LocalDateTime endTime; // 时间结束
    private String pairName; // 合约交易对
    private String orderCategory; // 委托分类: OPEN（开仓）, CLOSE（平仓）, ALL（全部）
    private String orderDirection; // 委托方向: OPEN_LONG（开多）, OPEN_SHORT（开空）, ALL（全部）
    private String orderType; // 委托类型: LIMIT（限价）, MARKET（市价）, STOP_LOSS（止盈止损）等, ALL（全部）
    private Boolean isLiquidation; // 是否爆仓单: true是, false否, null全部
    private Boolean isPlannedOrder; // 是否计划委托: true是, false否, null全部
    private String status; // 委托状态: PENDING（委托中）, CANCELLED（已撤销）, FILLED（委托成功）, FAILED（委托失败）, ALL（全部）
    private Long agentId; // 所属的代理商ID
    
    // 分页参数
    private Integer page = 0;
    private Integer size = 20;
}














