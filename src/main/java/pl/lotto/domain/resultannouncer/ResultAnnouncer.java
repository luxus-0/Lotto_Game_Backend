package pl.lotto.domain.resultannouncer;

import lombok.Data;

import java.util.UUID;

@Data
class ResultAnnouncer {
    private UUID uuid;
    private String status;
}
