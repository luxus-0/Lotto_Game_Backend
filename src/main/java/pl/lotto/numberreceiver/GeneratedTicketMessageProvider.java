package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.TicketMessageDto;

public class GeneratedTicketMessageProvider {
    public static final String GENERATED_TICKET_OK = "ok";
    public static final String GENERATED_TICKET_FAILED = "failed";

    static String generated_ticket_message_failed(){
        return GENERATED_TICKET_FAILED;
    }

    static String generated_ticket_message_ok(){
        return GENERATED_TICKET_OK;
    }
}
