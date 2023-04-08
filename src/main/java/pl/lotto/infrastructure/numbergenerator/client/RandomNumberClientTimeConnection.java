package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@NoArgsConstructor
public class RandomNumberClientTimeConnection {
    @Value("${numbers.genenerator.connectionTimeout}")
    private long connectionTimeOut;
    @Value("${numbers.genenerator.readTimeOut}")
    private long readTimeOut;
}
