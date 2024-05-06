package io.github.alfaio.afconfig.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LimMF
 * @since 2024/5/5
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;

}
