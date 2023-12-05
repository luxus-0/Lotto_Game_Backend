package pl.lotto.domain.login;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.lotto.domain.login.dto.RegisterUserDto;
import pl.lotto.domain.login.dto.RegistrationResultDto;
import pl.lotto.domain.login.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginFacade = new LoginAndRegisterFacade(
        new InMemoryLoginRepository()
    );

    @Test
    public void should_register_user(){
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");

        //when
        RegistrationResultDto register = loginFacade.register(registerUserDto);

        //then
        assertAll(
                () -> assertThat(register.created()).isTrue(),
                () -> assertThat(register.username()).isEqualTo("username"));

    }

    @Test
    public void should_find_user_by_user_name(){
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password");
        RegistrationResultDto register = loginFacade.register(registerUserDto);

        //when
        UserDto user = loginFacade.findByUsername(register.username());

        //then
        assertThat(user).isEqualTo(new UserDto(user.id(), "username", "password"));
    }

    @Test
    public void should_throw_exception_when_user_not_found(){
        //given
        String username = "user1";

        //when
        Throwable thrown = catchThrowable(() -> loginFacade.findByUsername(username));

        //then
        assertThat(thrown)
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("User not found");
    }

}