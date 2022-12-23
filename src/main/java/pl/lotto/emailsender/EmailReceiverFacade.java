package pl.lotto.emailsender;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Log4j2
public class EmailReceiverFacade {

    private final EmailSender emailSender;

    public EmailReceiverFacade(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    private String createEmailMessage(ResultsLottoDto resultsLotto) {
        return "Lotto id: " + UUID.randomUUID() +
                "numbers winner: " + resultsLotto.winnerNumbers() +
                "date winner: " + resultsLotto.dateTimeDraw().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

    public void sendEmailToClient(ResultsLottoDto resultsLotto) throws Exception {
        if(resultsLotto != null) {
            emailSender.send();
            String message = createEmailMessage(resultsLotto);
            log.info(message);
        }
        throw new Exception("Email is not send");
    }
}
