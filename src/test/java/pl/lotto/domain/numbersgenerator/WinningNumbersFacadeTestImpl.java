package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

public class WinningNumbersFacadeTestImpl {
    public static boolean isNumbersInRange(Set<Integer> numbers) {
         if(isInRange(numbers)){
             return true;
         }
         throw new OutOfRangeNumbersException("Numbers out of range");
    }

    private static boolean isInRange(Set<Integer> numbers) {
        return numbers.stream().allMatch(number -> number >= 1 && number <= 99);
    }
}
