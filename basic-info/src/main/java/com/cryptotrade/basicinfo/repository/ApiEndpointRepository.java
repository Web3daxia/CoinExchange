/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.repository;

import com.cryptotrade.basicinfo.entity.ApiEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiEndpointRepository extends JpaRepository<ApiEndpoint, Long> {
    List<ApiEndpoint> findByEndpointTypeAndStatusOrderByPriorityAsc(String endpointType, String status);

    List<ApiEndpoint> findByStatusOrderByPriorityAsc(String status);

    Optional<ApiEndpoint> findByIsDefaultTrueAndEndpointTypeAndStatus(String endpointType, String status);
}















