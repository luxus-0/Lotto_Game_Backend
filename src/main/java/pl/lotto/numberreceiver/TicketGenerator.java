package pl.lotto.numberreceiver;

import pl.lotto.date_time_generator.DateTimeGenerator;

import java.time.LocalDateTime;
import java.util.Set;

class TicketGenerator {
    private final UuidGenerator uuidGenerator;
    private final DateTimeGenerator dateTimeGenerator;

    TicketGenerator(UuidGenerator uuidGenerator, DateTimeGenerator dateTimeGenerator) {
        this.uuidGenerator = uuidGenerator;
        this.dateTimeGenerator = dateTimeGenerator;
    }

    Ticket generateTicket(Set<Integer> inputNumbers) {
        String uuid = uuidGenerator.generateUUID();
        LocalDateTime currentTime = dateTimeGenerator.getCurrentDateAndTime();
        LocalDateTime currentDrawTime = dateTimeGenerator.getDrawDate(currentTime);
        LocalDateTime expirationDate = dateTimeGenerator.getExpirationDateAndTime(currentTime);
        return generate(inputNumbers, uuid, currentTime, currentDrawTime, expirationDate);
    }

    private Ticket generate(Set<Integer> inputNumbers, String hash, LocalDateTime currentTime, LocalDateTime currentDrawTime, LocalDateTime expirationDate) {
        return Ticket.builder()
                .hash(hash)
                .numbers(inputNumbers)
                .dateTime(currentTime)
                .dateTime(currentDrawTime)
                .dateTime(expirationDate)
                .build();
    }
}
