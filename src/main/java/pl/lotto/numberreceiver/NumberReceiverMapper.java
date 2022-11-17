package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.NumberReceiverDto;

class NumberReceiverMapper {
    static NumberReceiverDto toDto(UserNumbers userNumbers){
        return new NumberReceiverDto(userNumbers.uuid(), userNumbers.numbersFromUser(), userNumbers.dateTimeDraw());
    }
}
