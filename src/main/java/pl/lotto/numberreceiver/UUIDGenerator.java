package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class UUIDGenerator {
    UUID generateUUID() {
        return UUID.randomUUID();
    }
}
