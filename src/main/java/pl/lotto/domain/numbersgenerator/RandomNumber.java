package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Builder
record RandomNumber(String uuid, Set<Integer> randomNumbers, String message) {
}
