package io.github.alfaio.afconfig.client.config;

import io.github.alfaio.afconfig.client.repository.AFRepository;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> keys = calcChangeKeys(this.config, event.config());
        if (keys.isEmpty()) {
            System.out.println("[AFCONFIG] not any change, ignore update.");
            return;
        }
        this.config = event.config();
        System.out.println("[AFCONFIG] publish EnvironmentChangeEvent with keys: " + keys);
        context.publishEvent(new EnvironmentChangeEvent(keys));
    }

    private Set<String> calcChangeKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
        if (oldConfigs.isEmpty()) {
            return newConfigs.keySet();
        }
        if (newConfigs.isEmpty()) {
            return oldConfigs.keySet();
        }
        Set<String> newKeys = newConfigs.keySet().stream()
                .filter(key -> !newConfigs.get(key).equals(oldConfigs.get(key)))
                .collect(Collectors.toSet());
        oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(newKeys::add);
        return newKeys;
    }
}
