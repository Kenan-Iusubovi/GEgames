package ge.games.gegames.repository;

import ge.games.gegames.entity.verification_code.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode,String> {

}
