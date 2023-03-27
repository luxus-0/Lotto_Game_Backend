package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

class WinningNumberMapper {
    static WinningNumbers toWinningNumbers(WinningNumbersDto winningNumbersDto){
        return WinningNumbers.builder()
                .hash("1234")
                .winningNumbers(winningNumbersDto.winningNumbers())
                .date(winningNumbersDto.date())
                .build();
    }
}
