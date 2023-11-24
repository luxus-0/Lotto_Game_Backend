package pl.lotto.domain.numbersgenerator;

import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketMessageDto;

@Service
public class WinningTicketMessage {
    public WinningTicketMessageDto generateWiningTicketMessage(WinningTicket winningTicket) {
        return WinningTicketMessageDto.builder()
                .message(System.out.printf("Ticket id: %s " +
                                        " Winning numbers: %s " +
                                        " Draw date: %s" +
                                        " Message: %s",
                                winningTicket.ticketId(),
                                winningTicket.winningNumbers(),
                                winningTicket.drawDate(),
                                winningTicket.message())
                        .toString())
                .build();
    }
}
