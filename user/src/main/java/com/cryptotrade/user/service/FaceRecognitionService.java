/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

public interface FaceRecognitionService {
    /**
     * 人脸比对
     * @param idCardImageUrl 身份证照片URL
     * @param videoUrl 视频URL
     * @return 比对分数（0-1之间，1表示完全匹配）
     */
    Double compareFace(String idCardImageUrl, String videoUrl);

    /**
     * 从视频中提取人脸特征
     * @param videoUrl 视频URL
     * @return 是否成功提取
     */
    boolean extractFaceFromVideo(String videoUrl);

    /**
     * 验证视频中的语音是否匹配验证语句
     * @param videoUrl 视频URL
     * @param expectedStatement 期望的验证语句
     * @return 是否匹配
     */
    boolean verifyStatement(String videoUrl, String expectedStatement);
}















