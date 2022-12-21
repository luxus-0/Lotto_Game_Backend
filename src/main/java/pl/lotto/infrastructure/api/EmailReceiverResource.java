package pl.lotto.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.emailsender.EmailReceiverFacade;

@RestController
class EmailReceiverResource {

    private final EmailReceiverFacade emailReceiverFacade;

    EmailReceiverResource(EmailReceiverFacade emailReceiverFacade) {
        this.emailReceiverFacade = emailReceiverFacade;
    }

    @PostMapping("/send")
    ResponseEntity<String> sendEmail(){
       return emailReceiverFacade.sendEmailToUser();
    }
}
