package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClientDto;

@Builder
public record WinningNumbersGeneratorParamURLDto(int count,
                                                 int lowerBand,
                                                 int upperBand,
                                                 String format,
                                                 int base,
                                                 int numberColumn,
                                                 TimeConnectionClientDto timeConnection) {
}
