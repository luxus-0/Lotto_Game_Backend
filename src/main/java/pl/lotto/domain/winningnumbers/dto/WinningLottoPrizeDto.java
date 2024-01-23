package pl.lotto.domain.winningnumbers.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public record WinningLottoPrizeDto(
        @Min(0)
        @Max(6)
        int numberOfHit,
        BigDecimal payoutAmount) {
}
