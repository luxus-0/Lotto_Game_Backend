package pl.lotto.numbersgenerator;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Data
@Document(collection = "numbersgenerator")
class NumbersGenerator {
    private UUID uuid;
    private Set<Integer> lottoNumbers;
}
