package pl.lotto.domain.numbersgenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

@Builder
public record RandomNumberDto(Set<Integer> randomNumbers){
}
