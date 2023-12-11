package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import pl.lotto.domain.resultchecker.ResultCheckerResponse;

import java.util.List;

@Builder
public record ResultCheckerResponseDto(List<ResultCheckerResponse> ticketsWinningNumbersSaved) {
}
