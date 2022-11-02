package site.xleon.platform.config.app;

import lombok.Data;

@Data
public class JWTConfig {
    /**
     * secret key
     */
    private String secret;

    /**
     * expiry minutes
     */
    private Integer expiry;
}
