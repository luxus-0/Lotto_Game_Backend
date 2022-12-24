package pl.lotto.emailsender;

import pl.lotto.emailsender.dto.MailMessageDto;

public interface EmailSender {
    MailMessageDto sendEmail();

    MailMessageDto sendEmailWithAttachment(String attachment);
}
