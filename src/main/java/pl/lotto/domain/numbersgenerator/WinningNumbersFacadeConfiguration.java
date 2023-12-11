package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClient;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClientValidator;

import static pl.lotto.domain.numbersgenerator.RandomNumbersURL.*;

@Configuration
public class WinningNumbersFacadeConfiguration {

    @Bean
    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository, WinningNumbersConfigurationProperties properties, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator(properties);
        RandomNumberGeneratorClientValidator randomNumberClientValidator = new RandomNumberGeneratorClientValidator(properties);
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumberGeneratorClient(new RestTemplate(), properties, randomNumberClientValidator);
        return WinningTicketFacade.builder()
                .drawDateFacade(drawDateFacade)
                .winningNumbersRepository(winningNumbersRepository)
                .winningNumbersValidator(winningNumbersValidator)
                .numberReceiverFacade(numberReceiverFacade)
                .randomNumbersGenerator(randomNumbersGenerator)
                .build();
    }

    public WinningTicketFacade winningNumbersFacade(DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersConfigurationProperties properties = getWinningNumbersConfigurationProperties();
        return winningNumbersFacade(drawDateFacade, winningNumbersRepository, properties, numberReceiverFacade);
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
