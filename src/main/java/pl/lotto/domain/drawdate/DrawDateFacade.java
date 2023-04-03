package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DrawDateFacade {
    private final DrawDateGenerator drawDateGenerator;
    public LocalDateTime retrieveNextDrawDate(){
        return drawDateGenerator.generateNextDrawDate();
    }
}
