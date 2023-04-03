package pl.lotto.domain.resultchecker;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.Set;

@Log4j2
@Service
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
        else if(!isInRange(winnerNumbers)) {
            throw new NotInRangeNumbersException("Winning numbers not in range");
        }
        else if(!isCorrectSize(winnerNumbers)){
            throw new NotCorrectSizeNumbersException("Winning numbers are not correct size");
        }
        return ResultDto.builder().build();
    }

    private boolean isCorrectSize(Set<Integer> winnerNumbers) {
        return winnerNumbers.size() == QUANTITY_NUMBERS;
    }

    private boolean isInRange(Set<Integer> winnerNumbers) {
        return winnerNumbers.stream().anyMatch(number -> number >= MIN_NUMBERS && number <= MAX_NUMBERS);
    }

}
