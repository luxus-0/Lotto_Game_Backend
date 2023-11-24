package pl.lotto.domain.numberreceiver.dto;

import javax.validation.constraints.NotNull;
import java.util.Set;

public record NumberReceiverRequestDto(
        @NotNull(message = "{input.numbers.not.null}")
        Set<Integer> inputNumbers) {
}
