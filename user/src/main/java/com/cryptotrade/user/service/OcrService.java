/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import java.util.Map;

public interface OcrService {
    /**
     * OCR识别证件信息
     * @param imageUrl 图片URL
     * @param idType 证件类型: ID_CARD, PASSPORT, DRIVER_LICENSE
     * @return OCR识别结果（Map包含姓名、证件号、出生日期等信息）
     */
    Map<String, String> recognizeIdCard(String imageUrl, String idType);
}















