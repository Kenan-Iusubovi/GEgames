package ge.games.gegames.dto.user.request;

import ge.games.gegames.validation.EmailOrPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {

    @EmailOrPhone(message = "Email or phone number required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;
}
