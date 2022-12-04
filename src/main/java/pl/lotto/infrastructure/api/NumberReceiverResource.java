package pl.lotto.infrastructure.api;

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
class NumberReceiverResource {

    private final NumberReceiverFacade numberReceiverFacade;

    NumberReceiverResource(NumberReceiverFacade numberReceiverFacade) {
        this.numberReceiverFacade = numberReceiverFacade;
    }

    @GetMapping("/numbers")
    ResponseEntity<NumberReceiverDto> inputNumbers(@RequestBody NumberReceiverDto numberReceiverDto) {
        Set<Integer> responseNumbers = numberReceiverDto.numbersFromUser();
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(responseNumbers);
        return new ResponseEntity<>(numberReceiver, HttpStatus.OK);
    }

    @GetMapping("/users")
    ResponseEntity<AllUsersNumbersDto> readUsers(@RequestBody NumberReceiverDto numberReceiverDto) {
        Set<Integer> responseNumbers = numberReceiverDto.numbersFromUser();
        LocalDateTime responseDateTime = numberReceiverDto.dateTimeDraw();
        AllUsersNumbersDto allUsersNumbersDto = numberReceiverFacade.usersNumbers(responseNumbers, responseDateTime);
        return new ResponseEntity<>(allUsersNumbersDto, HttpStatus.OK);
    }
}
