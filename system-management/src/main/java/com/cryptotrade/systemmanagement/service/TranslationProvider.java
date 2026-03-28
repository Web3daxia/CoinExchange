/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

/**
 * 翻译服务提供者接口
 */
public interface TranslationProvider {
    /**
     * 翻译内容
     * @param title 标题
     * @param summary 摘要
     * @param content 内容
     * @param targetLanguage 目标语言
     * @return 翻译结果
     * @throws Exception 翻译异常
     */
    TranslationResult translate(String title, String summary, String content, String targetLanguage) throws Exception;

    /**
     * 翻译结果
     */
    class TranslationResult {
        private String translatedTitle;
        private String translatedSummary;
        private String translatedContent;

        public TranslationResult(String translatedTitle, String translatedSummary, String translatedContent) {
            this.translatedTitle = translatedTitle;
            this.translatedSummary = translatedSummary;
            this.translatedContent = translatedContent;
        }

        public String getTranslatedTitle() {
            return translatedTitle;
        }

        public void setTranslatedTitle(String translatedTitle) {
            this.translatedTitle = translatedTitle;
        }

        public String getTranslatedSummary() {
            return translatedSummary;
        }

        public void setTranslatedSummary(String translatedSummary) {
            this.translatedSummary = translatedSummary;
        }

        public String getTranslatedContent() {
            return translatedContent;
        }

        public void setTranslatedContent(String translatedContent) {
            this.translatedContent = translatedContent;
        }
    }
}












