package ge.games.gegames.service;

import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.entity.user.User;
import ge.games.gegames.exception.UserAlreadyExistsException;
import ge.games.gegames.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;



    public Optional<User> findUserByMail(String mail){
        return repository.findByMail(mail);
    }

    public void isUserExistsOrThrow(String mail){
       if (repository.findByMail(mail).isPresent()){
           throw UserAlreadyExistsException.forField("EMAIL", mail);
       }
    }

    public UserDto createUser(UserRegistrationDto dto){

        Objects.requireNonNull(dto, "User registration data corrupted");

        isUserExistsOrThrow(dto.getMail());

        User user = User.registration(dto);
        user = repository.save(user);
        return UserDto.from(user);
    }
}
