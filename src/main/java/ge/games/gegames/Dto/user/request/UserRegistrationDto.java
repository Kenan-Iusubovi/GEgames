package ge.games.gegames.Dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank(message = "Nickname is required")
    @Size(min = 4, max = 20)
    private String nickname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String mail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be at least 8 characters and maximum 20")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[^\\d]).{8,}$",
            message = "Password must be at least 8 characters, include at least one number and one other character"
    )
    private String password;

}
