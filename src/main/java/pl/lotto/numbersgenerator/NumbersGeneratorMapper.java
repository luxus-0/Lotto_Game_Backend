package pl.lotto.numbersgenerator;

import pl.lotto.numbersgenerator.dto.LottoNumbersDto;

class NumbersGeneratorMapper {
    static LottoNumbersDto toDto(NumbersGenerator numbersGenerator) {
        return new LottoNumbersDto(numbersGenerator.uuid(), numbersGenerator.lottoNumbers());
    }
}
