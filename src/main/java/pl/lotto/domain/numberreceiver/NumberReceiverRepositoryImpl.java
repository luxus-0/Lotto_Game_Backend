package pl.lotto.domain.numberreceiver;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Component
class NumberReceiverRepositoryImpl {

    NumberReceiverRepository numberReceiverRepository;

    UserNumbers findUserByDateTime(LocalDateTime dateTime) {
        return numberReceiverRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.dateTimeDraw().equals(dateTime))
                .findAny()
                .orElse(null);
    }

    UserNumbers findUserByUUID(UUID uuid) {
        return numberReceiverRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.uuid().equals(uuid))
                .findAny()
                .orElse(new UserNumbers(null, null, null));
    }
}
