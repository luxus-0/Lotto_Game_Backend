package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
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
    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository, WinningNumbersConfigurationProperties properties, NumberReceiverFacade numberReceiverFacade, RandomNumbersGenerable generable) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator(properties);
        return WinningTicketFacade.builder()
                .drawDateFacade(drawDateFacade)
                .winningNumbersRepository(winningNumbersRepository)
                .winningNumberValidator(winningNumberValidator)
                .numberReceiverFacade(numberReceiverFacade)
                .randomNumbersGenerable(generable)
                .build();
    }

    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersConfigurationProperties properties = WinningNumbersConfigurationProperties.builder()
                .url("https://random.org/integers/?")
                .count(6)
                .lowerBand(1)
                .upperBand(99)
                .format("plain")
                .column(1)
                .base(10)
                .build();
        RandomNumbersGenerable generable = new InMemoryWinningNumbers();
        return winningNumbersFacade(drawDateFacade, winningNumbersRepository, properties, numberReceiverFacade, generable);
    }
}
