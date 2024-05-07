package io.github.alfaio.afconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.alfaio.afconfig.client.config.ConfigMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public class AFRepositoryImpl implements AFRepository {

    ConfigMeta meta;
    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public AFRepositoryImpl(ConfigMeta meta) {
        this.meta = meta;
        executor.scheduleWithFixedDelay(this::watch, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = meta.genKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }

    private Map<String, String> findAll() {
        String listPath = meta.listPath();
        System.out.println("[AFCONFIG] list all configs from " + listPath);
        List<Config> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Config>>() {
        });
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(config -> {
            resultMap.put(config.getPkey(), config.getPval());
        });
        return resultMap;
    }

    private void watch() {
        String versionPath = meta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, Long.class);
        String key = meta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if (version > oldVersion) {
            System.out.println("[AFCONFIG] current = " + version + ", old = " + oldVersion);
            System.out.println("[AFCONFIG] config changed");
            versionMap.put(key, version);
            configMap.put(key, findAll());
        }
    }
}
