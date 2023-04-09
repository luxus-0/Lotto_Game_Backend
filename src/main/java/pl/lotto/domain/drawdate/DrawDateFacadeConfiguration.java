package pl.lotto.domain.drawdate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DrawDateFacadeConfiguration {

    @Bean
    public DrawDateFacade createModuleForTests(AdjustableClock clock) {
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new DrawDateFacade(drawDateGenerator);
    }
}
