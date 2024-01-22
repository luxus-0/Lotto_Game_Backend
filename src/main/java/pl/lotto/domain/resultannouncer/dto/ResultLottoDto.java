package pl.lotto.domain.resultannouncer.dto;

public record ResultLottoDto(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto, WinningLottoPrizeDto winningLottoPrize, String message) {
}
