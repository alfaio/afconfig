package io.github.alfaio.afconfig.client.config;

import io.github.alfaio.afconfig.client.value.SpringValueProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public class AFConfigRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);
    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> clazz) {
        System.out.println("register " + clazz.getName());
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
        if (Arrays.stream(registry.getBeanDefinitionNames()).anyMatch(beanName -> beanName.equals(clazz.getName()))) {
            System.out.println(clazz.getName() + " already registered");
            return;
        }
        registry.registerBeanDefinition(clazz.getName(), beanDefinition);
    }
}
