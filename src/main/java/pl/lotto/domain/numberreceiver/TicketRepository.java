package pl.lotto.domain.numberreceiver;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findAllByDrawDate(LocalDateTime localDate);

    Ticket findByHash(String hash);
}
