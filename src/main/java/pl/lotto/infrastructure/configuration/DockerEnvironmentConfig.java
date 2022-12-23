package pl.lotto.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerEnvironmentConfig {

    @Value("${environment}")
    private String environment;
}
