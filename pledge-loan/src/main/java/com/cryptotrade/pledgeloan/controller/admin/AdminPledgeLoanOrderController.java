/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理 - 质押借币订单管理Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/order")
@Api(tags = "后台管理-质押借币订单")
public class AdminPledgeLoanOrderController {

    @Autowired
    private PledgeLoanOrderService orderService;

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取订单列表", notes = "获取质押借币订单列表（支持按状态筛选）")
    public Result<Page<PledgeLoanOrder>> getOrderList(
            @ApiParam(value = "状态") @RequestParam(required = false) String status,
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId,
            @ApiParam(value = "质押币种") @RequestParam(required = false) String pledgeCurrency,
            @ApiParam(value = "借款币种") @RequestParam(required = false) String loanCurrency,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Specification<PledgeLoanOrder> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (status != null && !status.isEmpty()) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (userId != null) {
                    predicates.add(cb.equal(root.get("userId"), userId));
                }
                if (pledgeCurrency != null && !pledgeCurrency.isEmpty()) {
                    predicates.add(cb.equal(root.get("pledgeCurrency"), pledgeCurrency));
                }
                if (loanCurrency != null && !loanCurrency.isEmpty()) {
                    predicates.add(cb.equal(root.get("loanCurrency"), loanCurrency));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            Page<PledgeLoanOrder> orders = orderRepository.findAll(spec, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "获取订单详情", notes = "获取订单详情")
    public Result<PledgeLoanOrder> getOrderById(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            PledgeLoanOrder order = orderService.getOrderById(orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/approve/{orderId}")
    @ApiOperation(value = "审批通过", notes = "审批通过质押借币订单")
    public Result<PledgeLoanOrder> approveOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId,
            @ApiParam(value = "审批人ID", required = true) @RequestHeader("adminId") Long approverId,
            @ApiParam(value = "审批状态", defaultValue = "MANUAL") @RequestParam(required = false, defaultValue = "MANUAL") String approvalStatus,
            @ApiParam(value = "备注") @RequestParam(required = false) String remark) {
        try {
            PledgeLoanOrder order = orderService.approveOrder(orderId, approverId, approvalStatus, remark);
            return Result.success("审批通过", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reject/{orderId}")
    @ApiOperation(value = "审批拒绝", notes = "拒绝质押借币订单")
    public Result<PledgeLoanOrder> rejectOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId,
            @ApiParam(value = "审批人ID", required = true) @RequestHeader("adminId") Long approverId,
            @ApiParam(value = "备注", required = true) @RequestParam String remark) {
        try {
            PledgeLoanOrder order = orderService.rejectOrder(orderId, approverId, remark);
            return Result.success("审批拒绝", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/check-risk")
    @ApiOperation(value = "检查风险订单", notes = "检查并处理风险订单")
    public Result<Void> checkRiskOrders() {
        try {
            orderService.checkAndProcessRiskOrders();
            return Result.success("风险检查完成", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

