/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.redpacket.dto.request.RedPacketUseRequest;
import com.cryptotrade.redpacket.entity.RedPacketUse;
import com.cryptotrade.redpacket.service.RedPacketUseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 红包使用Controller
 */
@RestController
@RequestMapping("/api/red-packet/use")
@Api(tags = "红包使用")
public class RedPacketUseController {

    @Autowired
    private RedPacketUseService useService;

    @PostMapping("/use")
    @ApiOperation(value = "使用红包", notes = "用户使用红包")
    public Result<RedPacketUse> usePacket(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "使用信息", required = true) @RequestBody RedPacketUseRequest request) {
        try {
            RedPacketUse use = useService.usePacket(userId, request);
            return Result.success("红包使用成功", use);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-uses")
    @ApiOperation(value = "获取我的使用记录", notes = "获取当前用户的所有红包使用记录")
    public Result<List<RedPacketUse>> getMyUses(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<RedPacketUse> uses = useService.getUserUses(userId);
            return Result.success(uses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














