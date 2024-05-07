package io.github.alfaio.afconfig.client.config;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public interface AFConfigService {

    String[] getPropertyNames();

    String getProperty(String name);
}
