package pl.lotto.emailsender;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lotto.emailsender.dto.EmailMessageDto;

public class EmailSenderFacade {
    private final EmailSender emailSender;

    public EmailSenderFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailMessageDto sendToClient(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        return emailSender.sendEmail(to, subject, text);
    }
}
