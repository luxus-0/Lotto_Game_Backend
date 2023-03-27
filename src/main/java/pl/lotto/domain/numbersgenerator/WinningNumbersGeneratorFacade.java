package pl.lotto.domain.numbersgenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {
    private final RandomNumberGeneratorFacade randomNumberGeneratorFacade;
    private final DrawDateFacade drawDateFacade;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;

    WinningNumbersDto generateWinningNumbers() throws JSONException, JsonProcessingException {
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = randomNumberGeneratorFacade.generateSixRandomNumbers().winningNumbers();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersCreator = WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(drawDate)
                .build();

        Set<Integer> winningNumbersSaved = winningNumbersRepository.save(winningNumbersCreator).winningNumbers();

        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbersSaved)
                .build();
    }
    WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime date) {
        Optional<WinningNumbers> winningNumbers = winningNumbersRepository.findWinningNumbersByDate(date);
        return winningNumbers.stream()
                .filter(Objects::nonNull)
                .map(WinningNumbersMapper::toDto)
                .findAny()
                .orElseThrow(WinningNumbersNotFoundException::new);
    }
}
