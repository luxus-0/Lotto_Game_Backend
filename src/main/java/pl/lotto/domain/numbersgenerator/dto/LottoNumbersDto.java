package pl.lotto.domain.numbersgenerator.dto;

import java.util.Set;

public record LottoNumbersDto(java.util.UUID uuid, Set<Integer> lottoNumbers) {
}
