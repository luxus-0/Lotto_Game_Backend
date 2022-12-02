package pl.lotto.numbersgenerator;

import org.springframework.stereotype.Service;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;

import java.util.UUID;

@Service
class DatabaseNumbersGeneratorRepositoryImpl {
    private final DatabaseNumbersGeneratorRepository numbersGeneratorRepository;

    DatabaseNumbersGeneratorRepositoryImpl(DatabaseNumbersGeneratorRepository numbersGeneratorRepository) {
        this.numbersGeneratorRepository = numbersGeneratorRepository;
    }

    LottoNumbersDto findNumbersGeneratorById(UUID uuid) {
        return numbersGeneratorRepository.findById(uuid)
                .map(NumbersGeneratorMapper::toDto)
                .orElseThrow(IllegalArgumentException::new);
    }

    LottoNumbersDto findNumbersGenerator() {
        return numbersGeneratorRepository.findAll()
                .stream()
                .map(NumbersGeneratorMapper::toDto)
                .findAny()
                .orElse(null);
    }

    NumbersGenerator createNumbersGenerator(NumbersGenerator numbersGenerator) {
        return numbersGeneratorRepository.save(numbersGenerator);
    }

    void removalNumbersGenerator() {
        numbersGeneratorRepository.deleteAll();
    }

    void removalNumbersByUUID(UUID uuid) {
        numbersGeneratorRepository.deleteById(uuid);
    }
}
