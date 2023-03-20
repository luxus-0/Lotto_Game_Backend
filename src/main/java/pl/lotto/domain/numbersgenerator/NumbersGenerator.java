package pl.lotto.domain.numbersgenerator;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Document
public record NumbersGenerator(UUID uuid, Set<Integer> lottoNumbers, LocalDateTime dateTimeDraw) {
}
