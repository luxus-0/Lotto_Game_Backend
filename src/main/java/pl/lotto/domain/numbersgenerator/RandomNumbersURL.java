package pl.lotto.domain.numbersgenerator;

public class RandomNumbersURL {
    public static final String BASE_RANDOM_NUMBERS_URL = "https://random.org/integers/?";
    public static final int COUNT_RANDOM_NUMBERS = 6;
    public static final int LOWER_BAND_RANDOM_NUMBERS = 1;
    public static final int UPPER_BAND_RANDOM_NUMBERS = 99;
    public static final String FORMAT_RANDOM_NUMBERS = "plain";
    public static final int COLUMN_RANDOM_NUMBERS = 2;
    public static final int BASE_RANDOM_NUMBERS = 10;
    public static final String RANDOM_NUMBERS_URL =
            BASE_RANDOM_NUMBERS_URL +
                    "num=" + COUNT_RANDOM_NUMBERS +
                    "&min=" + LOWER_BAND_RANDOM_NUMBERS +
                    "&max=" + UPPER_BAND_RANDOM_NUMBERS +
                    "&format=" + FORMAT_RANDOM_NUMBERS +
                    "&col=" + COLUMN_RANDOM_NUMBERS +
                    "&base=" + BASE_RANDOM_NUMBERS;
}
