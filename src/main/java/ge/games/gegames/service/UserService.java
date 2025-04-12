package ge.games.gegames.service;

import ge.games.gegames.entity.user.User;
import ge.games.gegames.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;



    public Optional<User> findUserByMail(String mail){
        return repository.findByMail(mail);
    }
}
