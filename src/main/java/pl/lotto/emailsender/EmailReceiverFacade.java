package pl.lotto.emailsender;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmailReceiverFacade {

    private final EmailSender emailSender;

    public EmailReceiverFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public ResponseEntity<String> sendEmailToUser() {
        emailSender.send();
        return ResponseEntity.ok("SENDING EMAIL...");
    }
}
