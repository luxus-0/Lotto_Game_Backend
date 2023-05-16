package pl.lotto.domain.numberreceiver.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public record NumberReceiverDto(
        @NotNull(message = "{input.numbers.not.null}")
        @NotEmpty(message = "{input.numbers.not.empty}") Set<Integer> inputNumbers) {
}
