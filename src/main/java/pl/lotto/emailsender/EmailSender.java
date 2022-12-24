package pl.lotto.emailsender;

import pl.lotto.emailsender.dto.EmailMessageDto;

import javax.mail.MessagingException;

public interface EmailSender {
    EmailMessageDto sendEmail(String to, String subject, String text);

    EmailMessageDto sendEmailWithAttachment(String to, String subject, String text, String attachment) throws MessagingException;
}
