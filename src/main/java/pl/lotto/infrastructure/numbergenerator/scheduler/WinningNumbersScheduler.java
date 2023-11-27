package pl.lotto.infrastructure.numbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;

/**
 * Generate random numbers every Saturday at 12 p.m
 */
@Component
@AllArgsConstructor
@Log4j2
public class WinningNumbersScheduler {

    private final WinningTicketFacade winningTicketFacade;

    @Scheduled(cron = "${numbers.generator.lottery.run.occurence}")
    public void generateWinningNumbers() {
        WinningTicketDto winningTicketDto = winningTicketFacade.generateWinningTicket();
        log.info(winningTicketDto.winningNumbers());
        log.info(winningTicketDto.drawDate());
    }
}
