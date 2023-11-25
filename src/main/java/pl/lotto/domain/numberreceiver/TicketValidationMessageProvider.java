package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numberreceiver.dto.TicketMessageDto;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketValidationMessageProvider {
    private final NumbersReceiverValidator validator;

    public TicketMessageDto getMessage() {
        List<TicketValidationResult> messagesResult = validator.errors;
        return messagesResult.stream()
                .map(TicketValidationResult::getInfo)
                .map(TicketMessageDto::new)
                .findAny()
                .orElse(TicketMessageDto.builder()
                        .message("")
                        .build());
    }
}
