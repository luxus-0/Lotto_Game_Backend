package pl.lotto.domain.numbersgenerator.dto;

import io.swagger.models.auth.In;

import java.util.Set;

public record RandomNumbersDto(Set<Integer> randomNumbers){
}
