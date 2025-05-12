package ge.games.gegames.dto.user.request;

import ge.games.gegames.validation.EmailOrPhone;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull(message = "Registration data is required")
public class UserRegistrationDto {

    @EmailOrPhone(message = "Email or phone number required")
    private String login;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[^\\d]).{8,}$",
            message = "Password must be at least 8 characters, include at least one number and one other character"
    )
    private String password;

    @NotBlank(message = "Language is required")
    @Pattern(regexp = "^[a-z]{2}$", message = "Language must be a two-letter lowercase code")
    private String language;

}
