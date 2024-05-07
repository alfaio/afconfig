package io.github.alfaio.afconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LimMF
 * @since 2024/5/7
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigMeta {

    String app;
    String env;
    String ns;
    String server;

    public String genKey() {
        return this.getApp() + "_" + this.getEnv() + "_" + this.getNs();
    }

    public String listPath() {
        return path("list");
    }

    public String versionPath() {
        return path("version");
    }

    private String path(String context) {
        return this.getServer() + "/" + context + "?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }

}
