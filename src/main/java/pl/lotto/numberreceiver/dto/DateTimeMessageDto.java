package pl.lotto.numberreceiver.dto;

import java.time.LocalDateTime;

public record DateTimeMessageDto(LocalDateTime drawDateTime, String message) {
}
