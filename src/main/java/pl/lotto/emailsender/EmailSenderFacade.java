package pl.lotto.emailsender;

import pl.lotto.emailsender.dto.EmailDetailsDto;

public class EmailSenderFacade {
    private final EmailSender emailSender;

    public EmailSenderFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public EmailMessage sendToClient(EmailDetailsDto emailDetails) {
        return emailSender.sendEmail(emailDetails.to(), emailDetails.subject(), emailDetails.text());
    }

    public EmailMessage sendToClientWithAttachment(EmailDetailsDto emailDetails) throws Exception {
        return emailSender.sendEmailWithAttachment(emailDetails.to(), emailDetails.subject(), emailDetails.text(), emailDetails.attachment());
    }
}
