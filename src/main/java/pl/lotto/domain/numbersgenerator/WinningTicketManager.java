package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class WinningTicketManager {

    private final WinningNumbersRepository winningNumbersRepository;

    public WinningTicket getWinnerTicket(String ticketId, Set<Integer> winningNumbers, LocalDateTime nextDrawDate) {
        return WinningTicket.builder()
                .ticketId(ticketId)
                .winningNumbers(winningNumbers)
                .drawDate(nextDrawDate)
                .message("WIN")
                .build();
    }

    public WinningTicketDto getSavedWinnerTicket(WinningTicket savedWinnerTicket) {
        return WinningTicketDto.builder()
                .ticketId(savedWinnerTicket.ticketId())
                .winningNumbers(savedWinnerTicket.winningNumbers())
                .drawDate(savedWinnerTicket.drawDate())
                .build();
    }

    public Set<Integer> getWinningNumbers(LocalDateTime nextDrawDate) throws WinnerNumbersNotFoundException {
        return winningNumbersRepository.findWinningNumbersByDrawDate(nextDrawDate).stream()
                .map(WinningTicket::winningNumbers)
                .findAny()
                .orElseThrow(WinnerNumbersNotFoundException::new);
    }
}
