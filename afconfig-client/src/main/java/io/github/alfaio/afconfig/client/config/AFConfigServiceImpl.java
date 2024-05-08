package io.github.alfaio.afconfig.client.config;

import io.github.alfaio.afconfig.client.repository.AFRepository;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public class AFConfigServiceImpl implements AFConfigService {

    ApplicationContext context;
    Map<String, String> config;

    public AFConfigServiceImpl(ApplicationContext context, Map<String, String> config) {
        this.context = context;
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

    @Override
    public void onChange(AFRepository.ChangeEvent event) {
        this.config = event.config();
        if (!config.isEmpty()) {
            System.out.println("[AFCONFIG] publish EnvironmentChangeEvent with keys: " + config.keySet());
            context.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
