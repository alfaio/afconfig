package io.github.alfaio.afconfig.client.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * af property source processor
 * @author LimMF
 * @since 2024/5/7
 **/
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    public static final String AF_PROPERTY_SOURCE = "AFPropertySource";
    public static final String AF_PROPERTY_SOURCES = "AFPropertySources";
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (env.getPropertySources().contains(AF_PROPERTY_SOURCES)) {
            return;
        }
        // 通过http，从config-server获取配置 todo
        Map<String, String> config = new HashMap<>();
        config.put("af.a", "dev100");
        config.put("af.b", "dev300");
        config.put("af.c", "dev500");
        AFConfigService configService = new AFConfigServiceImpl(config);
        AFPropertySource propertySource = new AFPropertySource(AF_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(AF_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        env.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
