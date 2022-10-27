package pl.lotto.numberreceiver;

import pl.lotto.datetimegenerator.DateTimeGeneratorFacade;

import java.time.LocalDateTime;
import java.util.Set;

class TicketGenerator {
    private final UuidGenerator uuidGenerator;
    private final DateTimeGeneratorFacade dateTimeGeneratorFacade;

    TicketGenerator(UuidGenerator uuidGenerator, DateTimeGeneratorFacade dateTimeGeneratorFacade) {
        this.uuidGenerator = uuidGenerator;
        this.dateTimeGeneratorFacade = dateTimeGeneratorFacade;
    }


    Ticket generateTicket(Set<Integer> inputNumbers) {
        String uuid = uuidGenerator.generateUUID();
        LocalDateTime actualDateTime = dateTimeGeneratorFacade.readActualDateTime();
        LocalDateTime actualDrawDateTime = dateTimeGeneratorFacade.readDrawDateTime();
        LocalDateTime expirationDateTime = dateTimeGeneratorFacade.readExpirationDateTime();
        return generate(inputNumbers, uuid, actualDateTime, actualDrawDateTime, expirationDateTime);
    }

    private Ticket generate(Set<Integer> inputNumbers, String hash, LocalDateTime currentTime, LocalDateTime currentDrawTime, LocalDateTime expirationDate) {
        return Ticket.builder()
                .hash(hash)
                .numbers(inputNumbers)
                .actualDate(currentTime)
                .drawDate(currentDrawTime)
                .expirationDate(expirationDate)
                .build();
    }
}
