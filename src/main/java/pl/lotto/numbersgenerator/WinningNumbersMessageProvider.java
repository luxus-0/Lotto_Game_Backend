package pl.lotto.numbersgenerator;

import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;

@Log4j2
class WinningNumbersMessageProvider {
    public static String winningNumbersNotFound(){
        return LoggerFactory.getLogger("Winning numbers not found").toString();
    }
}
