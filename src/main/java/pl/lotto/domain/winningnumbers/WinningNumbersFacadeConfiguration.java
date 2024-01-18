package pl.lotto.domain.winningnumbers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.randomnumbersgenerator.RandomNumbersGenerator;

import java.time.Instant;
import java.time.ZoneId;

import static pl.lotto.domain.randomnumbersgenerator.RandomNumbersURL.*;

@Configuration
public class WinningNumbersFacadeConfiguration {

    @Bean
    public WinningNumbersFacade winningNumbersFacade(RandomNumbersGenerator randomNumbersGenerator, WinningNumbersRepository winningNumbersRepository, WinningNumbersConfigurationProperties properties, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator(properties);
        WinningNumbersManager manager = new WinningNumbersManager();
        DrawDateFacade drawDateFacade = new DrawDateFacade(new DrawDateGenerator(new AdjustableClock(Instant.now(), ZoneId.systemDefault())));
        return WinningNumbersFacade.builder()
                .drawDateFacade(drawDateFacade)
                .winningNumbersRepository(winningNumbersRepository)
                .validator(winningNumbersValidator)
                .numberReceiverFacade(numberReceiverFacade)
                .randomNumbersGenerator(randomNumbersGenerator)
                .winningNumbersManager(manager)
                .build();
    }

    public WinningNumbersFacade winningNumbersFacade(RandomNumbersGenerator randomNumbersGenerator, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersConfigurationProperties properties = getWinningNumbersConfigurationProperties();
        return winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, properties, numberReceiverFacade);
    }

    public WinningNumbersConfigurationProperties getWinningNumbersConfigurationProperties() {
        return WinningNumbersConfigurationProperties.builder()
                .url(BASE_RANDOM_NUMBERS_URL)
                .count(COUNT_RANDOM_NUMBERS)
                .lowerBand(LOWER_BAND_RANDOM_NUMBERS)
                .upperBand(UPPER_BAND_RANDOM_NUMBERS)
                .format(FORMAT_RANDOM_NUMBERS)
                .column(COLUMN_RANDOM_NUMBERS)
                .base(BASE_RANDOM_NUMBERS)
                .build();
    }
}
