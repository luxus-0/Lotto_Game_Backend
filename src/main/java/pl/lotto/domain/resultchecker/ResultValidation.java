package pl.lotto.domain.resultchecker;

import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.Set;

public class ResultValidation {

    private static final int MIN_NUMBERS = 1;
    private static final int MAX_NUMBERS = 99;
    private static final int QUANTITY_NUMBERS = 6;

    ResultDto validate(Set<Integer> winnerNumbers){
        if(isInRange(winnerNumbers) && isCorrectSize(winnerNumbers)){
            return ResultDto.builder()
                    .numbers(winnerNumbers)
                    .build();
        }
        return ResultDto.builder()
                .message("incorrect range or size numbers")
                .build();
    }

    private boolean isCorrectSize(Set<Integer> winnerNumbers) {
        return winnerNumbers.size() == QUANTITY_NUMBERS;
    }

    private boolean isInRange(Set<Integer> winnerNumbers) {
        return winnerNumbers.stream().anyMatch(number -> number >= MIN_NUMBERS && number <= MAX_NUMBERS);
    }

}
