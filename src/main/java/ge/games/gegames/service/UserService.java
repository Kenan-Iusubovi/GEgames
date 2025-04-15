package ge.games.gegames.service;

import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.entity.user.User;
import ge.games.gegames.exception.UserAlreadyExistsException;
import ge.games.gegames.exception.UserNotFoundException;
import ge.games.gegames.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;



    public User findUserByMail(String mail){
        return repository.findByMail(mail).orElseThrow(
                () -> new UserNotFoundException(mail)
        );
    }

    public void isUserExistsOrThrow(String mail){
       if (repository.findByMail(mail).isPresent()){
           throw UserAlreadyExistsException.forField("EMAIL", mail);
       }
    }

    public UserDto createUser(UserRegistrationDto dto){

        Objects.requireNonNull(dto, "User registration data corrupted");

        isUserExistsOrThrow(dto.getMail());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(dto.getPassword());
        dto.setPassword(encryptedPassword);

        User user = User.registration(dto);
        user = repository.save(user);

        return UserDto.from(user, "Registration successful! A confirmation email has been sent to you." +
                " Please check your inbox and spam folder. If you haven't received it, contact support.");
    }
}
