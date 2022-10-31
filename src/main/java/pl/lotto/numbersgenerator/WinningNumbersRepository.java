package pl.lotto.numbersgenerator;

import pl.lotto.numberreceiver.Ticket;

import java.util.Set;

public interface WinningNumbersRepository {
    Set<Integer> save(Ticket winningNumbersTicket);
    Set<Integer> findWinningNumbers(String uuid);
}
