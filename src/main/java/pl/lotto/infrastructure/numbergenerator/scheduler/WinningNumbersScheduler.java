package pl.lotto.infrastructure.numbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;

/**
 * Generate random inputNumbers every Saturday at 12 p.m
 */
@AllArgsConstructor
@Log4j2
@Component
public class WinningNumbersScheduler {

    private final WinningTicketFacade winningTicketFacade;

    @Scheduled(cron = "${numbers.generator.lottery.run.occurence}")
    public void generateWinningNumbers() {
        WinningTicketResponseDto winningTicketResponseDto = winningTicketFacade.generateWinningTicket();
        log.info(winningTicketResponseDto.winningNumbers());
        log.info(winningTicketResponseDto.drawDate());
    }
}
