package io.github.alfaio.afconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
public class AFPropertySource extends EnumerablePropertySource<AFConfigService> {
    public AFPropertySource(String name, AFConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
