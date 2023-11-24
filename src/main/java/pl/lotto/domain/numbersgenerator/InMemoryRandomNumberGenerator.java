package pl.lotto.domain.numbersgenerator;

import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;

import java.util.Set;

@Log4j2
public class InMemoryRandomNumberGenerator implements RandomNumbersGenerable {
    @Override
    public RandomNumbersDto generateSixRandomNumbers() {
        return RandomNumbersDto.builder()
                .randomNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();
    }

    @Override
    public String generateUniqueTicketId() {
        return "123456";
    }

    public WinningTicketDto generateWinnerNumbers(Set<Integer> inputNumbers) {
        Set<Integer> randomNumbers = generateSixRandomNumbers().randomNumbers();
        Integer inputUserNumber = inputNumbers.stream().findAny().orElse(0);

        try {
            return getWinningNumbers(inputNumbers, randomNumbers, inputUserNumber);
        } catch (WinnerNumbersNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static WinningTicketDto getWinningNumbers(Set<Integer> inputNumbers, Set<Integer> randomNumbers, Integer inputUserNumber) throws WinnerNumbersNotFoundException {
        return randomNumbers.stream()
                .filter(isWinnerNumbers -> randomNumbers.contains(inputUserNumber))
                .map(numbers -> WinningTicketDto.builder()
                        .winningNumbers(inputNumbers)
                        .build())
                .findAny()
                .orElseThrow(WinnerNumbersNotFoundException::new);
    }
}
