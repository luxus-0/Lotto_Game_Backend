package pl.lotto.emailsender;

class EmailMessageProvider {
    public static final String FROM_EMAIL = "lotto_generator@op.pl";
    public static final String FROM_EMAIL_DESCRIPTION = "Lotto s.a";
    public static final String TO_EMAIL = "nowogorski.lukasz0@gmail.com";
    public static final String TO_EMAIL_DESCRIPTION = "Łukasz Nowogórski";
    public static final String SUBJECT = "Winner lotto game";
    public static final String TEXT = "CONGRATULATION!!!YOU WIN IN LOTTO GAME :D";

    public static final String HTML = "<img src='https://media.tenor.com/E6lFjorkDRAAAAAM/winner.gif'>" +
            "<b>YOU WIN!!</b>" +
            "<img src='https://www.icegif.com/wp-content/uploads/smile-icegif-1.gif'>";
}
