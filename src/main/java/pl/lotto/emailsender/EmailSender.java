package pl.lotto.emailsender;

import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Service;
import pl.lotto.emailsender.dto.MessageMailDto;

import static pl.lotto.emailsender.EmailConfigMessage.SMTP_HOST;
import static pl.lotto.emailsender.EmailConfigMessage.SMTP_PORT;
import static pl.lotto.emailsender.EmailSenderMessage.*;

@Service
@RequiredArgsConstructor
class EmailSender {

    private final EmailReader emailPasswordReader;

    MessageMailDto send() throws Exception {
        String PASSWORD = emailPasswordReader.readPasswordFromCSV();

        Email email = EmailBuilder.startingBlank()
                .from(FROM_EMAIL_DESCRIPTION, FROM_EMAIL)
                .to(TO_EMAIL_DESCRIPTION, TO_EMAIL)
                .withSubject(SUBJECT)
                .withHTMLText(HTML)
                .buildEmail();

        MailerBuilder
                .withSMTPServer(SMTP_HOST, SMTP_PORT, TO_EMAIL, PASSWORD)
                .async()
                .buildMailer()
                .sendMail(email);

        ConfigLoader.loadProperties("application.properties", true);

        return new MessageMailDto("SEND");
    }
}
