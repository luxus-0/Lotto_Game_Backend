package pl.lotto.emailsender.dto;

public class MailMessageDto {
    private final String message;

    public MailMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
