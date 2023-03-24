package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final NumberReceiverFacade numberReceiverFacade;


}
