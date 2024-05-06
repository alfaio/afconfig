package io.github.alfaio.afconfig.server;

import io.github.alfaio.afconfig.server.dal.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.github.alfaio.afconfig.server.model.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LimMF
 * @since 2024/5/5
 **/
@RestController
public class AFConfigController {

    Map<String, Long> VERSION = new HashMap<>();

    @Autowired
    ConfigMapper configMapper;

    @RequestMapping("/list")
    public List<Config> list(String app, String env, String ns){
        return configMapper.listByAppAndEnvAndNs(app, env, ns);
    }

    @RequestMapping("/update")
    public List<Config> update(@RequestParam("app") String app,
                               @RequestParam("env") String env,
                               @RequestParam("ns") String ns,
                               @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> {
            insertOrUpdate(new Config(app, env, ns, k, v));
        });
        VERSION.put(app + env + ns, System.currentTimeMillis());
        return configMapper.listByAppAndEnvAndNs(app, env, ns);
    }

    @GetMapping("/version")
    public Long version(String app, String env, String ns) {
        return VERSION.getOrDefault(app + env + ns, -1L);
    }

    private void insertOrUpdate(Config config) {
        Config conf = configMapper.select(config.getApp(), config.getEnv(), config.getNs(), config.getPkey());
        if (conf == null) {
            configMapper.insert(config);
        } else {
            configMapper.update(config);
        }
    }

}
