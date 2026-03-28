/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.financeproduct.dto.request.FinanceProductCreateRequest;
import com.cryptotrade.financeproduct.entity.FinanceProduct;
import com.cryptotrade.financeproduct.service.FinanceProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 理财产品管理Controller（后台管理）
 */
@RestController
@RequestMapping("/admin/finance/product")
@Api(tags = "理财产品管理")
public class FinanceProductController {

    @Autowired
    private FinanceProductService productService;

    @PostMapping("/create")
    @ApiOperation(value = "创建理财产品", notes = "创建新的理财产品")
    public Result<FinanceProduct> createProduct(
            @ApiParam(value = "产品信息", required = true) @RequestBody FinanceProductCreateRequest request) {
        try {
            FinanceProduct product = productService.createProduct(request);
            return Result.success("理财产品创建成功", product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update/{productId}")
    @ApiOperation(value = "更新理财产品", notes = "更新理财产品信息")
    public Result<FinanceProduct> updateProduct(
            @ApiParam(value = "产品ID", required = true) @PathVariable Long productId,
            @ApiParam(value = "产品信息", required = true) @RequestBody FinanceProductCreateRequest request) {
        try {
            FinanceProduct product = productService.updateProduct(productId, request);
            return Result.success("理财产品更新成功", product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    @ApiOperation(value = "删除理财产品", notes = "删除理财产品")
    public Result<Void> deleteProduct(
            @ApiParam(value = "产品ID", required = true) @PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return Result.success("理财产品删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有理财产品", notes = "获取所有理财产品列表")
    public Result<List<FinanceProduct>> getAllProducts() {
        try {
            List<FinanceProduct> products = productService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/available")
    @ApiOperation(value = "获取可用理财产品", notes = "获取可购买的理财产品列表")
    public Result<List<FinanceProduct>> getAvailableProducts() {
        try {
            List<FinanceProduct> products = productService.getAvailableProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{productId}")
    @ApiOperation(value = "获取理财产品详情", notes = "根据ID获取理财产品详情")
    public Result<FinanceProduct> getProductById(
            @ApiParam(value = "产品ID", required = true) @PathVariable Long productId) {
        try {
            FinanceProduct product = productService.getProductById(productId);
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














