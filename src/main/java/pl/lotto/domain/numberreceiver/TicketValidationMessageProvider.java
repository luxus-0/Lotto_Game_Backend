package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numberreceiver.dto.TicketMessageDto;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketValidationMessageProvider {
    private final NumbersReceiverValidator validator;

    public String getMessage() {
        return validator.errors.stream()
                .map(TicketValidationResult::getInfo)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Undefinied validation message"));
    }
}
