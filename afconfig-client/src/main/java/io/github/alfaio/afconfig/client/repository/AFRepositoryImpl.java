package io.github.alfaio.afconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.alfaio.afconfig.client.config.ConfigMeta;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
@AllArgsConstructor
public class AFRepositoryImpl implements AFRepository {

    ConfigMeta meta;

    @Override
    public Map<String, String> getConfig() {
        String listPath = meta.getServer() + "/list?app=" + meta.getApp()
                + "&env=" + meta.getEnv() + "&ns=" + meta.getNs();
        List<Config> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Config>>() {});
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(config -> {
            resultMap.put(config.getPkey(), config.getPval());
        });
        return resultMap;
    }
}
