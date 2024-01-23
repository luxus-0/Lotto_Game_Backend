package pl.lotto.domain.resultannouncer.dto;

import pl.lotto.domain.winningnumbers.dto.WinningLottoPrizeDto;

public record ResultLottoDto(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto, WinningLottoPrizeDto winningLottoPrize, String message) {
}
