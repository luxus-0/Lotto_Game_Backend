package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;
import pl.lotto.numberreceiver.dto.UserNumbersDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final DateTimeDrawGenerator dateTimeGenerator;
    private final UUIDGenerator uuidGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, DateTimeDrawGenerator dateTimeGenerator, UUIDGenerator uuidGenerator) {
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.dateTimeGenerator = dateTimeGenerator;
        this.uuidGenerator = uuidGenerator;
    }

    public NumberReceiverDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (!validate) {
            return new NumberReceiverDto(null, numbersFromUser, null);
        }
            UUID uuid = uuidGenerator.generateUUID();
            LocalDateTime dateTimeDraw = dateTimeGenerator.generateNextDrawDate();
            UserNumbers userNumbers = new UserNumbers(uuid, numbersFromUser, dateTimeDraw);
            UserNumbers savedUserNumbers = numberReceiverRepository.save(userNumbers);
            return new NumberReceiverDto(savedUserNumbers.uuid(), savedUserNumbers.numbersFromUser(), savedUserNumbers.dateTimeDraw());
        }

    public AllUsersNumbersDto usersNumbers(LocalDateTime date) {
        UUID uuid = uuidGenerator.generateUUID();
        UserNumbers usersByUUID = numberReceiverRepository.findByUUID(uuid);
        UserNumbers userByDate = numberReceiverRepository.findByDate(date);
        UUID id = usersByUUID.uuid();
        Set<Integer> numbersInput = usersByUUID.numbersFromUser();
        LocalDateTime dateDraw = userByDate.dateTimeDraw();
        return new AllUsersNumbersDto(List.of(new UserNumbersDto(id, numbersInput, dateDraw)));
    }
}