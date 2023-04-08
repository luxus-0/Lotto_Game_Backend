package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder
public record RandomNumberClientTimeConnection(@Value("${numbers.genenerator.connectionTimeout}") long connectionTimeOut,
                                               @Value("${numbers.genenerator.readTimeOut}") long readTimeOut) {
}
