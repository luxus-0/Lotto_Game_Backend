package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.enums.ValidateMessage;

class NumbersDuplicationInfo {
    static ValidateMessage duplicateNumbersInfo(){
        return ValidateMessage.DUPLICATE_NUMBERS;
    }

    static void countDuplicateNumbersInfo(long countNumbers){
        System.out.println("COUNT DUPLICATE NUMBERS: " +countNumbers);
    }
}
