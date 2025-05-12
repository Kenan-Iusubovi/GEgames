package ge.games.gegames.dto.user.request;

import ge.games.gegames.enums.LanguageE;
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

    private LanguageE language;

}
