package pl.lotto.numbersgenerator;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
class NumbersGenerator {
   private UUID uuid;
   private Set<Integer> lottoNumbers;
}
