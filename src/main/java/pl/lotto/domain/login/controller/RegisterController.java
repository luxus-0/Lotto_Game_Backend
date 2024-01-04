package pl.lotto.domain.login.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.login.LoginAndRegisterFacade;
import pl.lotto.domain.login.dto.RegisterUserDto;
import pl.lotto.domain.login.dto.RegistrationResultDto;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResultDto> register(@RequestBody @Valid RegisterUserDto registerUser){
        if(registerUser.username() == null){
            throw new RuntimeException("aaaaa");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(registerUser.password());
        RegistrationResultDto registerResult = loginAndRegisterFacade.register(
                new RegisterUserDto(registerUser.username(), encodedPassword));
        return ResponseEntity.status(CREATED).body(registerResult);
    }
}
