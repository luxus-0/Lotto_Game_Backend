package integration.numbergenerator.http;

public class WinningNumbersGeneratorWithTokenIntegrationTestConstants {
    public static String bodyWithNoWinningNumbersJson() {
        return """
                {
                  "winningNumbers" : []
                }
                """.trim();
    }

    public static String noWinningNumbersJson() {
        return """
                {
                  "ticketId":null,
                  "winningNumbers":[],
                  "drawDate":"2023-11-25T12:00:00",
                  "message":"NOT WIN"
                },
                "ticketId":null,
                  "winningNumbers":[],
                  "drawDate":"2023-11-25T12:00:00",
                  "message":"NOT WIN"
                }
                """.trim();
    }
    public static String REGEX_TOKEN = "^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$";
}
