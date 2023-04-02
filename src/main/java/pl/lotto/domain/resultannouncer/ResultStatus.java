package pl.lotto.domain.resultannouncer;

enum ResultStatus {
    HASH_NOT_EXIST("Ticket doesn't exist"),
    WAIT("Results are being calculated, please come back later"),
    WIN("Congratulation! You win"),
    LOSE("You lost, try again"),
    RESULT_MESSAGE_EXCEPTION("Result lotto not found");

    final String message;

    ResultStatus(String message) {
        this.message = message;

    }
}
