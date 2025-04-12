package ge.games.gegames.Dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String mail;

    @NotBlank(message = "Password is required")
    private String password;
}
