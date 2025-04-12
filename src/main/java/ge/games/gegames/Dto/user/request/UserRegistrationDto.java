package ge.games.gegames.Dto.user.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull(message = "Registration data is required")
public class UserRegistrationDto {

    @NotBlank(message = "Nickname is required")
    @Size(min = 4, max = 20)
    private String nickname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String mail;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[^\\d]).{8,}$",
            message = "Password must be at least 8 characters, include at least one number and one other character"
    )
    private String password;

}
