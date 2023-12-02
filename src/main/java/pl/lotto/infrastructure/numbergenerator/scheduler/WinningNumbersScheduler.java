package pl.lotto.infrastructure.numbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;

/**
 * Generate random numbers every Saturday at 12 p.m
 */
@Component
@AllArgsConstructor
@Log4j2
public class WinningNumbersScheduler {

    private final WinningNumbersFacade winningNumbersFacade;

    @Scheduled(cron = "${numbers.generator.lottery.run.occurence}")
    public void generateWinningNumbers() {
        WinningTicketResponseDto winningTicketResponseDto = winningNumbersFacade.generateWinningNumbers();
        log.info(winningTicketResponseDto.winningNumbers());
        log.info(winningTicketResponseDto.drawDate());
    }
}
