package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;
import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;
import pl.lotto.numberreceiver.dto.UserNumbersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final InMemoryNumberReceiverRepository inMemoryNumberReceiverRepository;
    private final NumberReceiverRepositoryImpl numberReceiverRepository;
    private final DateTimeDrawGenerator dateTimeGenerator;
    private final UUIDGenerator uuidGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, NumberReceiverRepositoryImpl numberReceiverRepositoryImpl, DateTimeDrawGenerator dateTimeGenerator, UUIDGenerator uuidGenerator) {
        this.numberValidator = numberValidator;
        this.inMemoryNumberReceiverRepository = new InMemoryNumberReceiverImpl();
        this.numberReceiverRepository = new NumberReceiverRepositoryImpl();
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
        UserNumbers savedUserNumbers = inMemoryNumberReceiverRepository.save(userNumbers);
        return new NumberReceiverDto(savedUserNumbers.uuid(), savedUserNumbers.numbersFromUser(), savedUserNumbers.dateTimeDraw());
    }

    public AllUsersNumbersDto usersNumbers(Set<Integer> numbersInput, LocalDateTime dateTimeDraw) {
        UUID uuid = uuidGenerator.generateUUID();
        LocalDateTime dateTime = inMemoryNumberReceiverRepository.findByDate(dateTimeDraw);
        UserNumbers userNumbers = new UserNumbers(uuid, numbersInput, dateTime);
        UserNumbers saveUserNumbers = inMemoryNumberReceiverRepository.save(userNumbers);
        return new AllUsersNumbersDto(List.of(new UserNumbersDto(saveUserNumbers.uuid(), saveUserNumbers.numbersFromUser(), saveUserNumbers.dateTimeDraw())));
    }

    public UserNumbersDto readUserByDateTime(LocalDateTime dateTime) {
        UserNumbers userNumbers = numberReceiverRepository.findUserByDateTime(dateTime);
        return Stream.of(userNumbers)
                .map(dto -> new UserNumbersDto(userNumbers.uuid(), userNumbers.numbersFromUser(), userNumbers.dateTimeDraw()))
                .findAny()
                .orElseThrow();
    }

    public UserNumbersDto readUserByUUID(UUID uuid) {
        UserNumbers userNumbers = numberReceiverRepository.findUserByUUID(uuid);
        return Stream.of(new UserNumbersDto(userNumbers.uuid(), userNumbers.numbersFromUser(), userNumbers.dateTimeDraw()))
                .filter(user -> user.uuid() != null && user.userNumbersInput() != null && user.date() != null)
                .findAny()
                .orElse(null);
    }
}