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

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    private final FirebaseService firebaseService;



    public UserDto getUserByFirebaseToken(String token){
        String login = firebaseService.getLoginFromToken(token);

        if (isUserExists(login)){

            return UserDto.from(findUserByEmailOrPhone(login));
        }
        return createUser(token);
    }

    public UserDto getUserByLogin(String email){

        return UserDto.from(findUserByEmailOrPhone(email));
    }

    private User findUserByEmailOrPhone(String emailOrPhone){

        return repository.findByLogin(emailOrPhone).orElseThrow(
                () -> new UserNotFoundException(emailOrPhone)
        );
    }

    private boolean isUserExists(String emailOrPhone){
        return repository.findByLogin(emailOrPhone).isPresent();
    }

    private void isUserAlreadyExistsThrows(String emailOrPhone){

       if (isUserExists(emailOrPhone)){
           throw UserAlreadyExistsException.forField("EMAIL OR PHONE", emailOrPhone);
       }
    }

    public UserDto createUser(UserRegistrationDto dto){

        Objects.requireNonNull(dto, "User registration data corrupted");

        isUserAlreadyExistsThrows(dto.getLogin());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(dto.getPassword());
        dto.setPassword(encryptedPassword);

        User user = User.registration(dto);
        user = repository.save(user);

        return UserDto.from(user, "Registration successful! A confirmation email has been sent to you." +
                " Please check your inbox and spam folder. If you haven't received it, contact support.");
    }

    public UserDto createUser(String firebaseToken){

        User user = firebaseService.createUser(firebaseToken);
        user = repository.save(user);

        return UserDto.from(user);
    }
}
