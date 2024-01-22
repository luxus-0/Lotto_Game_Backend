package pl.lotto.domain.resultannouncer;

enum ResultAnnouncerMessage {
    WAIT("Results are being calculated, please come back later"),
    WIN("You won"),
    LOSE("You lost"),
    ALREADY_CHECKED("You have already checked your ticket, please come back later");

    final String message;

    ResultAnnouncerMessage(String message) {
        this.message = message;

    }
}
