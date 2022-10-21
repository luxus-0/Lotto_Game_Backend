package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.enums.ValidateMessageInfo;

class DuplicateNumbersMessage {
    static void duplicateNumbersInfo(){
        System.out.println(ValidateMessageInfo.DUPLICATE_NUMBERS);
    }

    static void countDuplicateNumbersInfo(long countNumbers){
        System.out.println("COUNT DUPLICATE NUMBERS: " +countNumbers);
    }
}
