package pl.lotto.emailsender;

import pl.lotto.emailsender.dto.MailMessageDto;

public interface EmailSender {
    MailMessageDto sendEmail(String to, String subject, String text);
    MailMessageDto sendEmailWithAttachment(String to, String subject, String text, String attachment);
}
