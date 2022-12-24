package pl.lotto.emailsender.dto;

public record EmailDetailsDto(String to, String subject, String text, String attachment) {
}
