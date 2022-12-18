package pl.lotto.numbersgenerator;

import org.springframework.stereotype.Component;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;
import pl.lotto.numbersgenerator.exception.NumbersLottoNotFoundException;

import java.util.UUID;

class NumbersGeneratorRepositoryImpl {

    private final NumbersGeneratorRepository numbersGeneratorRepository;

    NumbersGeneratorRepositoryImpl(NumbersGeneratorRepository numbersGeneratorRepository) {
        this.numbersGeneratorRepository = numbersGeneratorRepository;
    }

    LottoNumbersDto createLottoNumbers(NumbersGenerator numbersGenerator) {
        return numbersGeneratorRepository.save(numbersGenerator)
                .lottoNumbers()
                .stream()
                .map(dto -> new LottoNumbersDto(numbersGenerator.uuid(), numbersGenerator.lottoNumbers()))
                .findAny()
                .orElse(new LottoNumbersDto(UUID.randomUUID(), null));
    }

    LottoNumbersDto searchByUUID(UUID uuid) {
        return numbersGeneratorRepository.findById(uuid)
                .map(NumbersGeneratorMapper::toDto)
                .orElseThrow(NumbersLottoNotFoundException::new);
    }

    void removalByUUID(UUID uuid) {
        numbersGeneratorRepository.deleteById(uuid);
    }
}
