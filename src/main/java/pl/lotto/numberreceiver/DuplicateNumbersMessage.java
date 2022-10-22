package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.enums.ValidateMessageInfo;

class DuplicateNumbersMessage {
    static ValidateMessageInfo duplicateNumbersInfo(){
        return ValidateMessageInfo.DUPLICATE_NUMBERS;
    }

    static void countDuplicateNumbersInfo(long countNumbers){
        System.out.println("COUNT DUPLICATE NUMBERS: " +countNumbers);
    }
}
