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

@EnableAFConfig
@SpringBootApplication
@EnableConfigurationProperties({AFDemoConfig.class})
public class AfconfigDemoApplication {

	@Value("${af.a}")
	private String a;
	@Autowired
	AFDemoConfig demoConfig;
	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(AfconfigDemoApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(){
		return args -> {
			System.out.println(" ===>>> environment = " + environment.getActiveProfiles());
			System.out.println(" ===>>> a = " + a);
			System.out.println(" ===>>> configA = " + demoConfig.getA());
		};
	}

}
