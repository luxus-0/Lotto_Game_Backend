package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

class RandomNumbersMapper {
    static RandomNumber toRandomNumber(RandomNumberDto dto){
        return RandomNumber.builder()
                .uuid(dto.uuid())
                .randomNumbers(dto.randomNumbers())
                .build();
    }
}
