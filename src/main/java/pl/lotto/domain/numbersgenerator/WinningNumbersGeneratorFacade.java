package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {
    private final RandomNumberGeneratorFacade randomNumberGeneratorFacade;
    private final DrawDateFacade drawDateFacade;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;

    WinningNumbersDto generateWinningNumbers() {
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
        WinningNumbers winningNumbers = winningNumbersRepository.findWinningNumbersByDate(date);
        List<WinningNumbers> winningNumberList = new ArrayList<>();
        winningNumberList.add(winningNumbers);
        return winningNumberList.stream()
                .filter(Objects::nonNull)
                .map(WinningNumbersMapper::toDto)
                .findAny()
                .orElseThrow(WinningNumbersNotFoundException::new);
    }
}
