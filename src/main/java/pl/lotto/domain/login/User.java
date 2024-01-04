package pl.lotto.domain.login;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;

import static pl.lotto.infrastructure.apivalidation.ApiValidationConstants.MESSAGE_PASSWORD;
import static pl.lotto.infrastructure.apivalidation.ApiValidationConstants.REGEX_PASSWORD;

@Builder
@Document
public record User(@UUID String uuid,
            @Indexed(unique = true)
            @NotNull(message = "username not null")
            @NotEmpty(message = "username not empty")
            String username,
            @NotNull(message = "password not null")
            @NotEmpty(message = "password not empty")
            @Pattern(regexp = REGEX_PASSWORD,message = MESSAGE_PASSWORD)
            String password) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
