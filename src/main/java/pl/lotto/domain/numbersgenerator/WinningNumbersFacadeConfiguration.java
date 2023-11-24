package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;
import pl.lotto.infrastructure.numbergenerator.scheduler.WinningNumbersScheduler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

@Configuration
public class WinningNumbersFacadeConfiguration {

    @Bean
    Clock clock() {
        return new AdjustableClock(LocalDateTime.of(2022, 11, 19, 12, 0, 0).toInstant(UTC), ZoneId.systemDefault());
    }

    @Bean
    WinningNumbersRepository winningNumbersRepository() {
        return new InMemoryWinningNumbersRepository();
    }

    @Bean
    WinningNumbersScheduler winningNumbersScheduler(WinningTicketFacade winningTicketFacade) {
        return new WinningNumbersScheduler(winningTicketFacade);
    }

    @Bean
    RandomNumberClient randomNumberClient(RestTemplate restTemplate, WinningNumbersConfigurationProperties properties){
        return new RandomNumberClient(restTemplate, properties);
    }

    @Bean
    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator(properties);
        return WinningTicketFacade.builder()
                .drawDateFacade(drawDateFacade)
                .randomNumbersGenerable(generator)
                .winningNumbersRepository(winningNumbersRepository)
                .winningNumberValidator(winningNumberValidator)
                .build();
    }

    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository) {
        WinningNumbersConfigurationProperties properties = WinningNumbersConfigurationProperties.builder()
                .url("https://random.org/integers/?")
                .count(6)
                .lowerBand(1)
                .upperBand(99)
                .format("plain")
                .column(1)
                .base(10)
                .build();
        return winningNumbersFacade(drawDateFacade, generator, winningNumbersRepository, properties);
    }
}
