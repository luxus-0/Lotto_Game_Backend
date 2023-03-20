package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.LottoNumbersDto;

class NumbersGeneratorMapper {
    static LottoNumbersDto toDto(NumbersGenerator numbersGenerator) {
        return new LottoNumbersDto(numbersGenerator.uuid(), numbersGenerator.lottoNumbers());
    }
}
