/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新闻数据DTO（从API获取的新闻数据）
 */
@Data
public class NewsDataDTO {
    private String sourceArticleId; // 新闻源的文章ID
    private String title; // 标题
    private String summary; // 摘要
    private String content; // 内容
    private String author; // 作者
    private String coverImageUrl; // 封面图片
    private String originalUrl; // 原文链接
    private LocalDateTime publishTime; // 发布时间
    private List<String> tags; // 标签列表
    private String category; // 分类
}














