package pl.lotto.infrastructure.numbereceiver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.Ticket;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResultDto;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user_numbers")
class NumberReceiverController {
    private final NumberReceiverFacade numberReceiverFacade;

    @GetMapping
    ResponseEntity<NumberReceiverResultDto> inputNumbers(@RequestBody Ticket ticket) {
        Set<Integer> responseNumbers = ticket.numbersFromUser();
        NumberReceiverResultDto numberReceiverResultDto = numberReceiverFacade.inputNumbers(responseNumbers);
        return new ResponseEntity<>(numberReceiverResultDto, HttpStatus.OK);
    }

    /*@GetMapping("/users")
    ResponseEntity<AllUsersNumbersDto> readUsers(@RequestBody Ticket numberReceiverDto) {
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
    }*/
}
