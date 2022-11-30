package pl.lotto.numbersgenerator.dto;

import java.util.Set;

public class LottoNumbersDto {
    Set<Integer> lottoNumbers;

    public LottoNumbersDto(Set<Integer> lottoNumbers) {
        this.lottoNumbers = lottoNumbers;
    }
}
