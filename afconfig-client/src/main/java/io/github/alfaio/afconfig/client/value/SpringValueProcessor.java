package io.github.alfaio.afconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import io.github.alfaio.afconfig.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * process spring value
 * 1、扫描spring value, 保存起来
 * 2、在配置变更时，更新其值
 * @author LimMF
 * @since 2024/5/8
 **/
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceholderHelper HELPER = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();
    @Setter
    BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("[AFCONFIG] find spring @Value field: {}", field);
            Value value = field.getAnnotation(Value.class);
            HELPER.extractPlaceholderKeys(value.value()).forEach(key->{
                log.info("[AFCONFIG] find spring @Value key: {}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                VALUE_HOLDER.add(key, springValue);
            });
        });
        return bean;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(key->{
            log.info("[AFCONFIG] update spring value: {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (springValues == null || springValues.isEmpty()) {
                return;
            }
            springValues.forEach(springValue->{
                log.info("[AFCONFIG] update spring value {} for key {}", springValue, key);
                Object value = HELPER.resolvePropertyValue((ConfigurableBeanFactory) beanFactory, springValue.getBeanName(), springValue.getPlaceholder());
                log.info("[AFCONFIG] update spring value {} for holder {}", springValue, springValue.getPlaceholder());
                springValue.getField().setAccessible(true);
                try {
                    springValue.getField().set(springValue.getBean(), value);
                } catch (IllegalAccessException e) {
                    log.error("[AFCONFIG] update spring value error", e);
                }

            });
        });
    }
}
