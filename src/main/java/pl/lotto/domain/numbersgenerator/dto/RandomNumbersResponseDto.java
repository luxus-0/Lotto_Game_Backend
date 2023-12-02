package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record RandomNumbersResponseDto(Set<Integer> randomNumbers) {
}
