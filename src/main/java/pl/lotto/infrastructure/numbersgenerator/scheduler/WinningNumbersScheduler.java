package pl.lotto.infrastructure.numbersgenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;

/**
 * Generate random numbers every Saturday at 12 p.m
 */
@AllArgsConstructor
@Log4j2
@Component
public class WinningNumbersScheduler {

    private final WinningNumbersFacade winningNumbersFacade;

    @Scheduled(cron = "${numbers.generator.lottery.run.occurence}")
    public void generateWinningNumbers() {
        WinningTicketResponseDto winningTicketResponseDto = winningNumbersFacade.generateWinningNumbers();
        log.info(winningTicketResponseDto.winningNumbers());
        log.info(winningTicketResponseDto.drawDate());
    }
}
