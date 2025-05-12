package ge.games.gegames.service;

import ge.games.gegames.entity.verification_code.EmailVerificationCode;
import ge.games.gegames.exception.VerificationCodeException;
import ge.games.gegames.repository.EmailVerificationCodeRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final Duration EXPIRATION = Duration.ofMinutes(15);

    private final EmailVerificationCodeRepository repository;


    private void saveVerificationCode(String email, String code){

        repository.save(EmailVerificationCode.create(email,code));
    }

    public boolean verifyCode(String email, String inputCode){

        EmailVerificationCode storedCode = repository.findById(email)
                .orElseThrow(() -> VerificationCodeException.notFound(email));

        if (storedCode.getCreatedAt().plus(EXPIRATION).isBefore(LocalDateTime.now())) {

            throw VerificationCodeException.expired();
        }

        if (!storedCode.getCode().equals(inputCode)){

            throw VerificationCodeException.incorrectCode();
        }

        repository.delete(storedCode);

        return true;
    }

    public String createVerificationCode(@Email String email) {

        String verificationCode = UUID.randomUUID().toString();

        saveVerificationCode(email,verificationCode);

        return verificationCode;
    }

    public String updateVerificationCode (@Email String email){

        EmailVerificationCode storedCode = repository.findById(email)
                .orElseThrow(() -> VerificationCodeException.notFound(email));

        String verificationCode = UUID.randomUUID().toString();

        storedCode.setCode(verificationCode);
        storedCode.setCreatedAt(LocalDateTime.now());

        saveVerificationCode(email,verificationCode);

        return verificationCode;
    }
}
