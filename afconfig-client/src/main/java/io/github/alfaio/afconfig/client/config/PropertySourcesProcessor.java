package io.github.alfaio.afconfig.client.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * af property source processor
 *
 * @author LimMF
 * @since 2024/5/7
 **/
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware, PriorityOrdered {

    public static final String AF_PROPERTY_SOURCE = "AFPropertySource";
    public static final String AF_PROPERTY_SOURCES = "AFPropertySources";
    private ApplicationContext applicationContext;
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        if (ENV.getPropertySources().contains(AF_PROPERTY_SOURCES)) {
            return;
        }
        // 通过http，从config-server获取配置
        String app = ENV.getProperty("afconfig.app", "app1");
        String env = ENV.getProperty("afconfig.env", "dev");
        String ns = ENV.getProperty("afconfig.ns", "public");
        String server = ENV.getProperty("afconfig.server", "http://localhost:9129");

        AFConfigService configService = AFConfigService.getDefault(applicationContext, new ConfigMeta(app, env, ns, server));
        AFPropertySource propertySource = new AFPropertySource(AF_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(AF_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        ENV.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
