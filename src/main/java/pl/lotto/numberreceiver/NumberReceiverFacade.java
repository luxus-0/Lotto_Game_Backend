package pl.lotto.numberreceiver;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NumberReceiverFacade {

    private final Clock clock;
    private final NumbersReceiverValidator numberValidator;
    private final NumberReceiverRepository numberReceiverRepository;
    private final DateTimeDrawGenerator dateTimeDrawGenerator;
    private final UUIDGenerator uuidGenerator;

    public NumberReceiverFacade(Clock clock, NumbersReceiverValidator numberValidator, NumberReceiverRepository numberReceiverRepository, DateTimeDrawGenerator dateTimeDrawGenerator, UUIDGenerator uuidGenerator) {
        this.clock = clock;
        this.numberValidator = numberValidator;
        this.numberReceiverRepository = numberReceiverRepository;
        this.dateTimeDrawGenerator = dateTimeDrawGenerator;
        this.uuidGenerator = uuidGenerator;
    }

    public NumberReceiverDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        if(!validate){
            return new NumberReceiverDto(null, numbersFromUser, null);
        }
        UUID uuid = uuidGenerator.generateUUID();
        LocalDateTime dateTimeDraw = dateTimeDrawGenerator.generateNextDrawDate();
        UserNumbers userNumbers = new UserNumbers(uuid, numbersFromUser, dateTimeDraw);
        UserNumbers save = numberReceiverRepository.save(userNumbers);
        return new NumberReceiverDto(save.uuid(), save.numbersFromUser(), save.dateDraw());
    }

    public AllUsersNumbersDto usersNumbers(LocalDateTime date){
        UUID uuid = uuidGenerator.generateUUID();
        UserNumbers userByUUID = numberReceiverRepository.findByUUID(uuid);
        UserNumbers userByDate = numberReceiverRepository.findByDate(date);
        UUID id = userByUUID.uuid();
        Set<Integer> numbersInput = userByUUID.numbersFromUser();
        LocalDateTime dateDraw = userByDate.dateDraw();
        return new AllUsersNumbersDto(List.of(new UserNumbersDto(id, numbersInput, dateDraw)));
    }
}