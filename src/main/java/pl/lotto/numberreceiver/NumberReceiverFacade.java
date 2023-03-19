package pl.lotto.numberreceiver;

import org.springframework.stereotype.Service;
import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.ResultDto;
import pl.lotto.numberreceiver.dto.UserNumbersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.FAILED_MESSAGE;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.SUCCESS_MESSAGE;

@Service
public class NumberReceiverFacade {

    private final NumbersReceiverValidator numberValidator;
    private final InMemoryNumberReceiverRepository inMemoryNumberReceiverRepository;
    private final NumberReceiverRepositoryImpl numberReceiverRepository;
    private final DateTimeDrawGenerator dateTimeGenerator;
    private final UUIDGenerator uuidGenerator;

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, DateTimeDrawGenerator dateTimeGenerator, UUIDGenerator uuidGenerator) {
        this.numberValidator = numberValidator;
        this.inMemoryNumberReceiverRepository = new InMemoryNumberReceiverImpl();
        this.numberReceiverRepository = new NumberReceiverRepositoryImpl();
        this.dateTimeGenerator = dateTimeGenerator;
        this.uuidGenerator = uuidGenerator;
    }

    public ResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (!validate) {
            return new ResultDto(numbersFromUser, FAILED_MESSAGE);
        }
        UUID uuid = uuidGenerator.generateUUID();
        LocalDateTime dateTimeDraw = dateTimeGenerator.generateNextDrawDate();
        UserNumbers userNumbers = new UserNumbers(uuid, numbersFromUser, dateTimeDraw);
        UserNumbers savedUserNumbers = inMemoryNumberReceiverRepository.save(userNumbers);
        Set<Integer> numbers = savedUserNumbers.numbersFromUser();
        return new ResultDto(numbers, SUCCESS_MESSAGE);
    }

    public AllUsersNumbersDto usersNumbers(LocalDateTime dateTimeDraw) {
        UserNumbers userNumbers = inMemoryNumberReceiverRepository.findByDate(dateTimeDraw);
        UserNumbers saveUserNumbers = inMemoryNumberReceiverRepository.save(new UserNumbers(userNumbers.uuid(), userNumbers.numbersFromUser(), userNumbers.dateTimeDraw()));
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