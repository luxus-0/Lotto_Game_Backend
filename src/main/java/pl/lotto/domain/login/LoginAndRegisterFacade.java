package pl.lotto.domain.login;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.lotto.domain.login.dto.RegisterUserDto;
import pl.lotto.domain.login.dto.RegistrationResultDto;
import pl.lotto.domain.login.dto.UserDto;

import java.util.UUID;

@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    public static final String USER_NOT_FOUND = "User not found";
    private final LoginRepository repository;

    public UserDto findByUsername(String username){
        return repository.findByUsername(username)
                .map(user -> new UserDto(user.uuid(), user.username(), user.password()))
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND));
    }

    public RegistrationResultDto register(RegisterUserDto registerUser){
        final User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .username(registerUser.username())
                .password(registerUser.password())
                .build();
        User savedUser = repository.save(user);
        return new RegistrationResultDto(savedUser.uuid(), savedUser.username(), true);
    }
}
