/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service;

import com.cryptotrade.miningpool.entity.HashrateType;

import java.util.List;

/**
 * 算力类型管理Service接口
 */
public interface HashrateTypeService {
    
    /**
     * 创建算力类型
     */
    HashrateType createHashrateType(HashrateType hashrateType);
    
    /**
     * 更新算力类型
     */
    HashrateType updateHashrateType(Long id, HashrateType hashrateType);
    
    /**
     * 删除算力类型
     */
    void deleteHashrateType(Long id);
    
    /**
     * 根据ID获取算力类型
     */
    HashrateType getHashrateTypeById(Long id);
    
    /**
     * 根据代码获取算力类型
     */
    HashrateType getHashrateTypeByCode(String typeCode);
    
    /**
     * 获取所有算力类型
     */
    List<HashrateType> getAllHashrateTypes();
    
    /**
     * 获取激活的算力类型
     */
    List<HashrateType> getActiveHashrateTypes();
    
    /**
     * 根据算法获取算力类型
     */
    List<HashrateType> getHashrateTypesByAlgorithm(String algorithm);
}














