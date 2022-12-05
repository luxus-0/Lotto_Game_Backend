package pl.lotto.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;
import pl.lotto.numberreceiver.dto.UserNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

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
        LocalDateTime responseDateTime = numberReceiverDto.dateTimeDraw();
        AllUsersNumbersDto allUsersNumbersDto = numberReceiverFacade.usersNumbers(responseDateTime);
        return new ResponseEntity<>(allUsersNumbersDto, HttpStatus.OK);
    }

    @GetMapping("/user/{dateTime}")
    ResponseEntity<UserNumbersDto> findUser(@PathVariable LocalDateTime dateTime) {
        UserNumbersDto userNumbersDto = numberReceiverFacade.readUserByDateTime(dateTime);
        return ResponseEntity.ok(userNumbersDto);
    }

    @GetMapping("/user/{uuid}")
    ResponseEntity<UserNumbersDto> findUser(@PathVariable UUID uuid) {
        UserNumbersDto userNumbersDto = numberReceiverFacade.readUserByUUID(uuid);
        return new ResponseEntity<>(userNumbersDto, HttpStatus.OK);
    }
}
