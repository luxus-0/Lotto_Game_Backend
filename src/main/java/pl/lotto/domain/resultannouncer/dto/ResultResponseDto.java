package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import pl.lotto.domain.resultchecker.dto.ResultDto;

@Builder
public record ResultResponseDto(ResultDto resultDto, String message) {
}