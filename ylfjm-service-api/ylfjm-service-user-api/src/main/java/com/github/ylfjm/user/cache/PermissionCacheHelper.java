package com.github.ylfjm.user.cache;

import com.github.ylfjm.user.dto.PermissionCacheDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 描述：权限缓存类，只在单机部署下使用，集群部署请使用redis实现
 *
 * @Author Zhang Bo
 * @Date 2020/10/24
 */
public class PermissionCacheHelper {

    private static final String P_CACHE = "p_cache_";
    private static final String R_CACHE = "r_cache_";

    private static Map<String, Set<PermissionCacheDTO>> pMap = new HashMap<>();
    private static Map<String, Set<PermissionCacheDTO>> rMap = new HashMap<>();


    public static Set<PermissionCacheDTO> getPList(Integer suffix) {
        return pMap.get(P_CACHE + suffix);
    }

    public static Set<PermissionCacheDTO> setPList(Integer suffix, Set<PermissionCacheDTO> permissions) {
        return pMap.put(P_CACHE + suffix, permissions);
    }

    public static Set<PermissionCacheDTO> getRList(Integer suffix) {
        return rMap.get(R_CACHE + suffix);
    }

    public static Set<PermissionCacheDTO> setRList(Integer suffix, Set<PermissionCacheDTO> permissions) {
        return rMap.put(R_CACHE + suffix, permissions);
    }

}
