package io.github.alfaio.afconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LimMF
 * @since 2024/5/6
 **/
@Data
@ConfigurationProperties(prefix = "af")
public class AFDemoConfig {

    private String a;
}
