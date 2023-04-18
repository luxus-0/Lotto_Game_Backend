package pl.lotto.infrastructure.numbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

@AllArgsConstructor
@Log4j2
public class WinningNumbersScheduler {

    private final WinningNumbersFacade winningNumbersFacade;

    @Scheduled(cron = "${numbers.generator.lottery.run.occurence}")
    public void generateWinningNumbers() {
        log.info("Winning numbers scheduler started");
        WinningNumbersDto winningNumbersDto = winningNumbersFacade.generateWinningNumbers();
        log.info(winningNumbersDto.winningNumbers());
        log.info(winningNumbersDto.drawDate());
    }
}
