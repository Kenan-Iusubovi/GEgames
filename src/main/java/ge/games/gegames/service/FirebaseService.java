package ge.games.gegames.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import ge.games.gegames.entity.user.Role;
import ge.games.gegames.entity.user.User;
import ge.games.gegames.enums.UserStatusE;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FirebaseService {

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {

        return FirebaseAuth.getInstance().verifyIdToken(token);
    }

    public String getEmailFromToken(String token) {

        try {
            FirebaseToken firebaseToken = verifyToken(token);

            return firebaseToken.getEmail();

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid firebase token" + e);
        }
    }

    public User createUser(String token) {

        try {
            FirebaseToken firebaseToken = verifyToken(token);

            return User.builder()
                    .id(0)
                    .nickname("firebaseToken.getName")
                    .login(firebaseToken.getEmail())
                    .roles(Set.of(new Role(2, "USER")))
                    .userStatusE(UserStatusE.BRONZE)
                    .build();

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid firebase token" + e);
        }
    }

}
