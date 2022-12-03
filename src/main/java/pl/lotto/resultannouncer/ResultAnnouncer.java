package pl.lotto.resultannouncer;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
class ResultAnnouncer {
    private UUID uuid;
    private String status;
}
