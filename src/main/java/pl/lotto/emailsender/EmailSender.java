package pl.lotto.emailsender;

import javax.mail.MessagingException;

public interface EmailSender {
    EmailMessage sendEmail(String to, String subject, String text);

    EmailMessage sendEmailWithAttachment(String to, String subject, String text, String attachment) throws MessagingException;
}
