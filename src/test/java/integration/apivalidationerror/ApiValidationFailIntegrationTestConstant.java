package integration.apivalidationerror;

public class ApiValidationFailIntegrationTestConstant {
    public static String bodyWithNoWinningNumbersJson() {
        return """
                        {
                          "winningNumbers" : []
                        }
                        """.trim();
    }
    public static String REGEX_TOKEN ="^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$";
}
