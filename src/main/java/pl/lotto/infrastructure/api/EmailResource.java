package pl.lotto.infrastructure.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.emailsender.EmailDetails;
import pl.lotto.emailsender.EmailMessage;
import pl.lotto.emailsender.EmailSenderFacade;

@RestController
@Log4j2
class EmailResource {

    private final EmailSenderFacade emailSenderFacade;

    EmailResource(EmailSenderFacade emailSenderFacade) {
        this.emailSenderFacade = emailSenderFacade;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    EmailMessage sendEmail(@RequestBody EmailDetails emailDetails) {
        try {
            return emailSenderFacade.sendToClient(emailDetails);
        } catch (Exception e) {
            return new EmailMessage("SEND EMAIL");
        }
    }

    @PostMapping("/send/attachment")
    @ResponseStatus(HttpStatus.CREATED)
    EmailMessage sendEmailWithAttachment(@RequestBody EmailDetails emailDetails) throws Exception {
        return emailSenderFacade.sendToClientWithAttachment(emailDetails);
    }
}
