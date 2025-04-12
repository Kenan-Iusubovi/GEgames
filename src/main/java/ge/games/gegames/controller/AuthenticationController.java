package ge.games.gegames.controller;

import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService service;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
        public UserDto createUser(@Valid @RequestBody UserRegistrationDto dto){
        return service.createUser(dto);
    }
}
