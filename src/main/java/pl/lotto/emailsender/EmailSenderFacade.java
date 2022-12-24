package pl.lotto.emailsender;

import org.springframework.stereotype.Service;

@Service
public class EmailSenderFacade {
    private final EmailSender emailSender;

    public EmailSenderFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public EmailMessage sendToClient(EmailDetails emailDetails) {
        return emailSender.sendEmail(emailDetails);
    }

    public EmailMessage sendToClientWithAttachment(EmailDetails emailDetails) throws Exception {
        return emailSender.sendEmailWithAttachment(emailDetails);
    }
}
