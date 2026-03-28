/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.dto.response;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * 质押借币订单响应DTO
 */
@Data
public class PledgeLoanOrderResponse {
    private Long id;
    private String orderNo;
    private Long userId;
    private String pledgeCurrency;
    private BigDecimal pledgeAmount;
    private BigDecimal pledgeValue;
    private String loanCurrency;
    private BigDecimal loanAmount;
    private BigDecimal loanValue;
    private BigDecimal interestRate;
    private Integer loanTermDays;
    private BigDecimal pledgeRatio;
    private String status;
    private String approvalStatus;
    private BigDecimal remainingPrincipal;
    private BigDecimal totalInterest;
    private BigDecimal paidInterest;
    private BigDecimal liquidationPrice;
    private BigDecimal healthRate;
    private String remark;

    public static PledgeLoanOrderResponse fromEntity(PledgeLoanOrder order) {
        PledgeLoanOrderResponse response = new PledgeLoanOrderResponse();
        BeanUtils.copyProperties(order, response);
        return response;
    }
}














