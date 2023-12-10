package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import pl.lotto.domain.resultannouncer.ResultLotto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record PlayerResultsDto(@NotNull @NotEmpty List<ResultLotto> results) {
}
