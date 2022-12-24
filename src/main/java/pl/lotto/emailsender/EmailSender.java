package pl.lotto.emailsender;

public interface EmailSender {
    void sendEmail();
    void sendEmailWithAttachment(String attachment);
}
