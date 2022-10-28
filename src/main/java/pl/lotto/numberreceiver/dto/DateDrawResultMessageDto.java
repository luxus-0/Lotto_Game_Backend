package pl.lotto.numberreceiver.dto;

import java.time.LocalDateTime;

public record DateDrawResultMessageDto(LocalDateTime dateDraw, String message){}
