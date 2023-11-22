package pl.lotto.domain.numbersgenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

@Builder
public record RandomNumbersDto(Set<Integer> randomNumbers) {
}
