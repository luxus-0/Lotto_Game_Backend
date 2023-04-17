package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import pl.lotto.domain.resultchecker.dto.ResultDto;

@Builder
public record ResultAnnouncerResponseDto(ResultDto resultDto, String message) {
}
