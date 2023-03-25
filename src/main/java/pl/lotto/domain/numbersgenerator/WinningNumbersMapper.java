package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

class WinningNumbersMapper {

    static WinningNumbersDto toDto(WinningNumbers winningNumbers) {
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers.winningNumbers())
                .date(winningNumbers.date())
                .build();
    }
}
