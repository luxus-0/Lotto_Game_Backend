package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultLottoDto;
import pl.lotto.domain.winningnumbers.dto.WinningLottoPrizeDto;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static pl.lotto.domain.resultannouncer.PriceAmount.BASE_PRICE;
import static pl.lotto.domain.resultannouncer.NumbersFactor.NUMBERS_FACTOR;
import static pl.lotto.domain.resultannouncer.NumbersInRange.COUNT_NUMBERS;
import static pl.lotto.domain.resultannouncer.ResultAnnouncerMapper.mapToResultLottoSaved;
import static pl.lotto.domain.resultannouncer.ResultAnnouncerMessage.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultAnnouncerRepository resultAnnouncerRepository;
    private final Clock clock;

    @Cacheable("results")
    public ResultAnnouncerResponseDto findResultAnnouncer(String ticketUUID) throws Exception {
        if (ticketUUID == null || ticketUUID.isEmpty()) {
            throw new TicketUUIDNotFoundException();
        }
        TicketResponseDto resultByTicketUUID = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        ResultAnnouncerResponse resultAnnouncerResponse = resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)
                .orElseThrow(() -> new ResultAnnouncerNotFoundException("Not found for ticket uuid: " + ticketUUID));
        ResultAnnouncerResponse resultSaved = resultAnnouncerRepository.save(resultAnnouncerResponse);
        ResultAnnouncerResponseDto resultLottoSaved = mapToResultLottoSaved(resultSaved);
        int hitNumbersCount = resultLottoSaved.hitNumbers().size();
        WinningLottoPrizeDto winningAmount = calculateWinningPrice(hitNumbersCount);
        if (!isAfterAnnouncementTime(resultByTicketUUID)) {
            new ResultLottoDto(resultLottoSaved, winningAmount, WAIT.message);
        }
        else if(isAfterAnnouncementTime(resultByTicketUUID)){
            new ResultLottoDto(resultLottoSaved, winningAmount, ALREADY_CHECKED.message);
        }
        else if (resultLottoSaved.isWinner()) {
            new ResultLottoDto(resultLottoSaved, winningAmount, WIN.message);
        }
        else {
            new ResultLottoDto(resultLottoSaved, winningAmount, LOSE.message);
        }

        return resultLottoSaved;
    }
    private WinningLottoPrizeDto calculateWinningPrice(int countHitNumbers) {
        if (countHitNumbers > 0 && countHitNumbers <= COUNT_NUMBERS) {
            double winningPrize = calculatePrize(countHitNumbers);
            return new WinningLottoPrizeDto(countHitNumbers, BigDecimal.valueOf(winningPrize));
        } else {
            return new WinningLottoPrizeDto(0, BigDecimal.ZERO);
        }
    }
    private double calculatePrize(int countHitNumbers) {
        return countHitNumbers * NUMBERS_FACTOR * BASE_PRICE;
    }

    private boolean isAfterAnnouncementTime(TicketResponseDto resultResponse) {
        LocalDateTime announcementDateTime = resultResponse.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
