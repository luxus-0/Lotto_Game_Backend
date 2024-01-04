package pl.lotto.infrastructure.apivalidation;

public class ApiValidationConstants {
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$";
    public static final String MESSAGE_PASSWORD =
            "At least one lowercase letter, \\n" +
            "At least one uppercase letter, \\n" +
            "At least one digit, \\n" +
            "At least one character";
}
