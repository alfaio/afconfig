package io.github.alfaio.afconfig.demo;

import io.github.alfaio.afconfig.client.annotation.EnableAFConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAFConfig
@RestController
@SpringBootApplication
@EnableConfigurationProperties({AFDemoConfig.class})
public class AfconfigDemoApplication {

    @Value("${af.a}")
    private String a;
    @Value("${af.a}")
    private String b;
    @Autowired
    AFDemoConfig demoConfig;
    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(AfconfigDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            System.out.println(" ===>>> environment = " + environment.getActiveProfiles());
            System.out.println(" ===>>> a = " + a);
            System.out.println(" ===>>> configA = " + demoConfig.getA());
        };
    }

    @GetMapping("/demo")
    public String demo() {
        return "af.a = " + a + "\n"
                + "af.b = " + b + "\n"
                + "demoConfig.af.a = " + demoConfig.getA() + "\n"
                + "demoConfig.af.b = " + demoConfig.getB();
    }

}
