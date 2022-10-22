package pl.lotto.numberreceiver;

import java.util.Collections;
import java.util.List;

class NumbersDuplicationChecker {
   boolean checkIdenticalNumbers(List<Integer> numbersInput){
      return numbersInput.stream()
              .anyMatch(duplicateNumbers -> Collections.frequency(numbersInput, duplicateNumbers) > 1);
   }
}
