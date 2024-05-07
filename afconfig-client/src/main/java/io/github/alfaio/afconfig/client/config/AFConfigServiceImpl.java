package io.github.alfaio.afconfig.client.config;

import java.util.Map;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public class AFConfigServiceImpl implements AFConfigService {

    Map<String, String> config;

    public AFConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }
    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }
}
