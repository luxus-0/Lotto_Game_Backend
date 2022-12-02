package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
class DatabaseNumberReceiverRepositoryImpl {
    
    private final DatabaseNumberReceiverRepository databaseNumberReceiverRepository;

    DatabaseNumberReceiverRepositoryImpl(DatabaseNumberReceiverRepository databaseNumberReceiverRepository) {
        this.databaseNumberReceiverRepository = databaseNumberReceiverRepository;
    }

    public UserNumbers findUserByDateTime(LocalDateTime dateTime) {
        return databaseNumberReceiverRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.dateTimeDraw().equals(dateTime))
                .findAny()
                .orElse(null);
    }
    
    public UserNumbers findUserByUUID(UUID uuid) {
        return databaseNumberReceiverRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.uuid().equals(uuid))
                .findAny()
                .orElse(new UserNumbers(null, null, null));
    }
}
