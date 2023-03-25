package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

class RandomNumberMapper {
    static RandomNumber toRandomNumber(RandomNumberDto dto){
        return RandomNumber.builder()
                .uuid("123456")
                .randomNumbers(dto.randomNumbers())
                .build();
    }
}
