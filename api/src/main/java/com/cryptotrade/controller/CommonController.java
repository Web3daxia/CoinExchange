/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.controller;

import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/common")
@Api(tags = "通用接口")
public class CommonController {

    @GetMapping("/countries")
    @ApiOperation(value = "获取国家/地区列表", notes = "返回支持的国家/地区及对应的电话区号")
    public Result<List<Map<String, String>>> getCountries() {
        List<Map<String, String>> countries = new ArrayList<>();
        
        Map<String, String> china = new HashMap<>();
        china.put("code", "CN");
        china.put("name", "中国");
        china.put("dialCode", "+86");
        countries.add(china);

        Map<String, String> usa = new HashMap<>();
        usa.put("code", "US");
        usa.put("name", "美国");
        usa.put("dialCode", "+1");
        countries.add(usa);

        Map<String, String> uk = new HashMap<>();
        uk.put("code", "GB");
        uk.put("name", "英国");
        uk.put("dialCode", "+44");
        countries.add(uk);

        // 可以添加更多国家...
        
        return Result.success(countries);
    }
}















