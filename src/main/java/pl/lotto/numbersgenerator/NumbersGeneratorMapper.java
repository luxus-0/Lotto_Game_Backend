package pl.lotto.numbersgenerator;

import java.util.Random;

import static pl.lotto.numbersgenerator.NumbersGeneratorMessageProvider.MAX_NUMBERS;
import static pl.lotto.numbersgenerator.NumbersGeneratorMessageProvider.MIN_NUMBERS;

class NumbersGeneratorMapper {
    public final static int RANDOM_NUMBERS = new Random().nextInt(MIN_NUMBERS, MAX_NUMBERS);
}
