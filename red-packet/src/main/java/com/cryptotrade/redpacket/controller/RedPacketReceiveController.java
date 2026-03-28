/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.redpacket.dto.request.RedPacketReceiveRequest;
import com.cryptotrade.redpacket.entity.RedPacketReceive;
import com.cryptotrade.redpacket.service.RedPacketReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 红包领取Controller
 */
@RestController
@RequestMapping("/api/red-packet/receive")
@Api(tags = "红包领取")
public class RedPacketReceiveController {

    @Autowired
    private RedPacketReceiveService receiveService;

    @PostMapping("/receive")
    @ApiOperation(value = "领取红包", notes = "用户领取红包")
    public Result<RedPacketReceive> receivePacket(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "领取信息", required = true) @RequestBody RedPacketReceiveRequest request) {
        try {
            RedPacketReceive receive = receiveService.receivePacket(userId, request);
            return Result.success("红包领取成功", receive);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-packets")
    @ApiOperation(value = "获取我的红包", notes = "获取当前用户的所有红包")
    public Result<List<RedPacketReceive>> getMyPackets(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<RedPacketReceive> packets = receiveService.getUserPackets(userId);
            return Result.success(packets);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-valid-packets")
    @ApiOperation(value = "获取我的有效红包", notes = "获取当前用户的所有有效红包")
    public Result<List<RedPacketReceive>> getMyValidPackets(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<RedPacketReceive> packets = receiveService.getUserValidPackets(userId);
            return Result.success(packets);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














