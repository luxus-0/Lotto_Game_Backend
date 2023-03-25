package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

class RandomNumbersMapper {
    static RandomNumber toRandomNumber(RandomNumbersDto dto){
        return RandomNumber.builder()
                .uuid("123456")
                .randomNumbers(dto.randomNumbers())
                .build();
    }
}
