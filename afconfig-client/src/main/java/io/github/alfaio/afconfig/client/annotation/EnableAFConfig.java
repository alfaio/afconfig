package io.github.alfaio.afconfig.client.annotation;

import io.github.alfaio.afconfig.client.config.AFConfigRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author LimMF
 * @since 2024/5/5
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(AFConfigRegister.class)
public @interface EnableAFConfig {
}
