package com.sdcompany.callpop.config.security.properties;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfig(
        @NotEmpty String secretKey
) {
}
