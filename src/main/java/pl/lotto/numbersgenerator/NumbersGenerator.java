package pl.lotto.numbersgenerator;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document(collection = "numbers_generator")
record NumbersGenerator(UUID uuid, Set<Integer> lottoNumbers) {
}
