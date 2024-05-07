package io.github.alfaio.afconfig.client.config;

import io.github.alfaio.afconfig.client.repository.AFRepository;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public interface AFConfigService {

    static AFConfigService getDefault(ConfigMeta meta) {
        AFRepository repository = AFRepository.getDefault(meta);
        return new AFConfigServiceImpl(repository.getConfig());
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
