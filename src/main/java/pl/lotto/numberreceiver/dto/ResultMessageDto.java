package pl.lotto.numberreceiver.dto;

import java.util.Set;

public record ResultMessageDto(Set<Integer> userNumbers, String message){
}
