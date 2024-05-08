package io.github.alfaio.afconfig.client.repository;

import io.github.alfaio.afconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public interface AFRepository {

    static AFRepository getDefault(ConfigMeta meta) {
        return new AFRepositoryImpl(meta);
    }

    Map<String, String> getConfig();

    void addListener(ChangeListener listener);

    interface ChangeListener {
        void onChange(ChangeEvent event);
    }

    record ChangeEvent(ConfigMeta meta, Map<String, String> config) {}
 }
