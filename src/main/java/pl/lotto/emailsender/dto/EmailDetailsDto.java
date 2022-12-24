package pl.lotto.emailsender.dto;

import lombok.Getter;

@Getter
public record EmailDetailsDto(String to, String subject, String text) {
}
