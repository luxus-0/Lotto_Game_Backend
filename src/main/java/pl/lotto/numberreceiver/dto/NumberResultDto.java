package pl.lotto.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record NumberResultDto(String ticketId, LocalDateTime drawDate, Set<Integer> numbersFromUser, String message){
}
