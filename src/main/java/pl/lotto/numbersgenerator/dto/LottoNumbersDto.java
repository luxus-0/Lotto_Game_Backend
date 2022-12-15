package pl.lotto.numbersgenerator.dto;

import java.util.Set;

public record LottoNumbersDto(java.util.UUID uuid, Set<Integer> lottoNumbers) {
}
