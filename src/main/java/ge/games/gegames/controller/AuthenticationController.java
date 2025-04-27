package ge.games.gegames.controller;

import ge.games.gegames.Dto.user.request.UserLoginRequestDto;
import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;

    @PostMapping("/registration")
        public UserDto createUser(@Valid @RequestBody UserRegistrationDto dto){
        return service.createUser(dto);
    }

    @PostMapping("/token/refresh")
        public ResponseEntity<Void> refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        service.processToken(request,response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
        public ResponseEntity<UserDto> login(@RequestBody @Valid UserLoginRequestDto request, HttpServletResponse response){
            UserDto user = service.login(request, response);
            return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
        public ResponseEntity<Void> logout(HttpServletResponse response){
        service.logout(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/firebase-login")
    public ResponseEntity<UserDto> firebaseLogin(@RequestParam String idToken, HttpServletResponse response){
        UserDto user = service.firebaseLogin(idToken, response);
        return ResponseEntity.ok(user);
    }
}
