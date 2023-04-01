package pl.lotto.domain.resultannouncer;

class ResultAnnouncerNotFoundException extends RuntimeException{
    ResultAnnouncerNotFoundException(String message){
        super(message);
    }
}
