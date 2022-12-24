package pl.lotto.emailsender;

public interface EmailSender {
    EmailMessage sendEmail(EmailDetails emailDetails);
    EmailMessage sendEmailWithAttachment(EmailDetails emailDetails) throws Exception;
}
