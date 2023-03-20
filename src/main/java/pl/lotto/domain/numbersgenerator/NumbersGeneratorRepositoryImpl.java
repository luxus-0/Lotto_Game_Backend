package pl.lotto.domain.numbersgenerator;

import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.LottoNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.NumbersLottoNotFoundException;

import java.util.UUID;

@Service
class NumbersGeneratorRepositoryImpl {

    NumbersGeneratorRepository numbersGeneratorRepository;

    LottoNumbersDto createUserLottoNumbers(NumbersGenerator numbersGenerator) {
        return numbersGeneratorRepository.save(numbersGenerator)
                .lottoNumbers()
                .stream()
                .map(dto -> new LottoNumbersDto(numbersGenerator.uuid(), numbersGenerator.lottoNumbers()))
                .findAny()
                .orElse(new LottoNumbersDto(UUID.randomUUID(), null));
    }

    LottoNumbersDto searchUserLottoNumbersByUUID(UUID uuid) {
        return numbersGeneratorRepository.findById(uuid)
                .map(NumbersGeneratorMapper::toDto)
                .orElseThrow(NumbersLottoNotFoundException::new);
    }

    void removalUserLottoNumbersByUUID(UUID uuid) {
        numbersGeneratorRepository.deleteById(uuid);
    }
}
