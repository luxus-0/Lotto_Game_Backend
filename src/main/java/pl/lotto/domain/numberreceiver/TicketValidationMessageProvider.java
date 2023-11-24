package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numberreceiver.dto.TicketValidationMessageDto;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketValidationMessageProvider {
    private final NumbersReceiverValidator validator;

    public TicketValidationMessageDto getMessage() {
        List<TicketValidationResult> messagesResult = validator.errors;
        return messagesResult.stream()
                .map(TicketValidationResult::getInfo)
                .map(TicketValidationMessageDto::new)
                .findAny()
                .orElse(TicketValidationMessageDto.builder()
                        .message("Ticket message undefinied")
                        .build());
    }
}
