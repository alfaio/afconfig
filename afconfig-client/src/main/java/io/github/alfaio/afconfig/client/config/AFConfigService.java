package io.github.alfaio.afconfig.client.config;

import io.github.alfaio.afconfig.client.repository.AFRepository;
import org.springframework.context.ApplicationContext;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public interface AFConfigService extends AFRepository.ChangeListener {

    static AFConfigService getDefault(ApplicationContext context, ConfigMeta meta) {
        AFRepository repository = AFRepository.getDefault(meta);
        AFConfigService configService = new AFConfigServiceImpl(context, repository.getConfig());
        repository.addListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
