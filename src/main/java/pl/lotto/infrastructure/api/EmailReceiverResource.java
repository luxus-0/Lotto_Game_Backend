package pl.lotto.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.emailsender.EmailReceiverFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

@RestController
class EmailReceiverResource {

    private final EmailReceiverFacade emailReceiverFacade;

    EmailReceiverResource(EmailReceiverFacade emailReceiverFacade) {
        this.emailReceiverFacade = emailReceiverFacade;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    void sendEmail(@RequestBody ResultsLottoDto resultsLotto) throws Exception {
        emailReceiverFacade.sendEmailToClient(resultsLotto);
    }
}
