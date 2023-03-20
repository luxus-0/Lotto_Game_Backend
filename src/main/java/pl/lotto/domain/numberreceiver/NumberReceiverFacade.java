package pl.lotto.domain.numberreceiver;

import org.springframework.stereotype.Service;
import pl.lotto.domain.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.domain.numberreceiver.dto.ResultMessageDto;
import pl.lotto.domain.numberreceiver.dto.UserNumbersDto;

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

    public NumberReceiverFacade(NumbersReceiverValidator numberValidator, DateTimeDrawGenerator dateTimeGenerator, UUIDGenerator uuidGenerator) {
        this.numberValidator = numberValidator;
        this.inMemoryNumberReceiverRepository = new InMemoryNumberReceiverImpl();
        this.numberReceiverRepository = new NumberReceiverRepositoryImpl();
        this.dateTimeGenerator = dateTimeGenerator;
        this.uuidGenerator = uuidGenerator;
    }

    public ResultMessageDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if (!validate) {
            return new ResultMessageDto(numbersFromUser, NumbersReceiverMessageProvider.FAILED_MESSAGE);
        }
        UUID uuid = uuidGenerator.generateUUID();
        LocalDateTime dateTimeDraw = dateTimeGenerator.generateNextDrawDate();
        UserNumbers userNumbers = new UserNumbers(uuid, numbersFromUser, dateTimeDraw);
        UserNumbers savedUserNumbers = inMemoryNumberReceiverRepository.save(userNumbers);
        Set<Integer> numbers = savedUserNumbers.numbersFromUser();
        return new ResultMessageDto(numbers, NumbersReceiverMessageProvider.SUCCESS_MESSAGE);
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