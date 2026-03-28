/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.Blacklist;
import java.util.List;

public interface BlacklistService {
    Blacklist createBlacklist(Blacklist blacklist);
    Blacklist updateBlacklist(Long id, Blacklist blacklist);
    void deleteBlacklist(Long id);
    Blacklist getBlacklistById(Long id);
    boolean isBlacklisted(String blacklistType, String blacklistValue);
    List<Blacklist> getAllBlacklists();
    List<Blacklist> getBlacklistsByType(String blacklistType);
    void removeExpiredBlacklists();
}














