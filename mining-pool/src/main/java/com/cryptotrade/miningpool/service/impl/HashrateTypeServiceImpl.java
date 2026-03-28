/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.service.impl;

import com.cryptotrade.miningpool.entity.HashrateType;
import com.cryptotrade.miningpool.repository.HashrateTypeRepository;
import com.cryptotrade.miningpool.service.HashrateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 算力类型管理Service实现
 */
@Service
public class HashrateTypeServiceImpl implements HashrateTypeService {

    @Autowired
    private HashrateTypeRepository hashrateTypeRepository;

    @Override
    @Transactional
    public HashrateType createHashrateType(HashrateType hashrateType) {
        if (hashrateTypeRepository.findByTypeCode(hashrateType.getTypeCode()).isPresent()) {
            throw new RuntimeException("算力类型代码已存在: " + hashrateType.getTypeCode());
        }

        if (hashrateType.getStatus() == null) {
            hashrateType.setStatus("ACTIVE");
        }

        return hashrateTypeRepository.save(hashrateType);
    }

    @Override
    @Transactional
    public HashrateType updateHashrateType(Long id, HashrateType hashrateType) {
        HashrateType existing = hashrateTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("算力类型不存在: " + id));

        if (hashrateType.getTypeName() != null) {
            existing.setTypeName(hashrateType.getTypeName());
        }
        if (hashrateType.getTypeCode() != null && !existing.getTypeCode().equals(hashrateType.getTypeCode())) {
            if (hashrateTypeRepository.findByTypeCode(hashrateType.getTypeCode()).isPresent()) {
                throw new RuntimeException("算力类型代码已存在: " + hashrateType.getTypeCode());
            }
            existing.setTypeCode(hashrateType.getTypeCode());
        }
        if (hashrateType.getAlgorithm() != null) {
            existing.setAlgorithm(hashrateType.getAlgorithm());
        }
        if (hashrateType.getUnit() != null) {
            existing.setUnit(hashrateType.getUnit());
        }
        if (hashrateType.getDescription() != null) {
            existing.setDescription(hashrateType.getDescription());
        }
        if (hashrateType.getStatus() != null) {
            existing.setStatus(hashrateType.getStatus());
        }

        return hashrateTypeRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteHashrateType(Long id) {
        if (!hashrateTypeRepository.existsById(id)) {
            throw new RuntimeException("算力类型不存在: " + id);
        }
        hashrateTypeRepository.deleteById(id);
    }

    @Override
    public HashrateType getHashrateTypeById(Long id) {
        return hashrateTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("算力类型不存在: " + id));
    }

    @Override
    public HashrateType getHashrateTypeByCode(String typeCode) {
        return hashrateTypeRepository.findByTypeCode(typeCode)
                .orElseThrow(() -> new RuntimeException("算力类型不存在: " + typeCode));
    }

    @Override
    public List<HashrateType> getAllHashrateTypes() {
        return hashrateTypeRepository.findAll();
    }

    @Override
    public List<HashrateType> getActiveHashrateTypes() {
        return hashrateTypeRepository.findByStatus("ACTIVE");
    }

    @Override
    public List<HashrateType> getHashrateTypesByAlgorithm(String algorithm) {
        return hashrateTypeRepository.findByAlgorithm(algorithm);
    }
}














