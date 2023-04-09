package pl.lotto.infrastructure.numbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

@Component
@AllArgsConstructor
@Log4j2
public class WinningNumbersScheduler {

    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    //@Scheduled
    public void generateWinningNumbers(){
       WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
       log.info(winningNumbersDto);
    }
}
