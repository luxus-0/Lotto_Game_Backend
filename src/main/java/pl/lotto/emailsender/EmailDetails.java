package pl.lotto.emailsender;

public record EmailDetails(String to, String subject, String text, String attachment) {
}
