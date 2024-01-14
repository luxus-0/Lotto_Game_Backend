package integration.cache.redis;

import java.time.LocalDateTime;

class RedisNumberReceiverConstant {
    public static String TICKET_UUID_REGEX = "[a-zA-Z0-9]";
    public static LocalDateTime DRAW_DATE = LocalDateTime.of(2023, 12, 16, 12, 0);
}
