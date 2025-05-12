package ge.games.gegames.entity.verification_code;

import ge.games.gegames.service.EmailVerificationService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_code")
@Data
@Builder()
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationCode {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public static EmailVerificationCode create(@Email String email, @NotBlank String code){

        return EmailVerificationCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
