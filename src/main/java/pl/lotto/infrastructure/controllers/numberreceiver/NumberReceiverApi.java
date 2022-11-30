package pl.lotto.infrastructure.controllers.numberreceiver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
public class NumberReceiverApi {

    private final NumberReceiverFacade numberReceiverFacade;

    public NumberReceiverApi(NumberReceiverFacade numberReceiverFacade) {
        this.numberReceiverFacade = numberReceiverFacade;
    }

    @GetMapping("/input_numbers")
    public ResponseEntity<NumberReceiverDto> inputNumbers(@RequestBody NumberReceiverDto numberReceiverDto) {
        Set<Integer> responseNumbers = numberReceiverDto.numbersFromUser();
        NumberReceiverDto numberReceiverResponse = numberReceiverFacade.inputNumbers(responseNumbers);
        return new ResponseEntity<>(numberReceiverResponse, HttpStatus.OK);
    }

    @GetMapping("/users_numbers")
    public ResponseEntity<AllUsersNumbersDto> usersNumbers(@RequestBody NumberReceiverDto numberReceiverDto) {
        Set<Integer> responseNumbers = numberReceiverDto.numbersFromUser();
        LocalDateTime responseDateTime = numberReceiverDto.dateTimeDraw();
        AllUsersNumbersDto allUsersNumbersDto = numberReceiverFacade.usersNumbers(responseNumbers, responseDateTime);
        return new ResponseEntity<>(allUsersNumbersDto, HttpStatus.OK);
    }
}
