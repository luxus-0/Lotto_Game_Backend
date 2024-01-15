package pl.lotto.infrastructure.security.token;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.lotto.domain.login.LoginAndRegisterFacade;
import pl.lotto.domain.login.dto.UserDto;

@AllArgsConstructor
@Service
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = loginAndRegisterFacade.findByUsername(username);
        return getUser(userDto);
    }

    private static UserDetails getUser(UserDto user) {
        return User.builder()
                .username(user.username())
                .password(user.password())
                .build();
    }
}
