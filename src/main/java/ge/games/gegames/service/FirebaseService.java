package ge.games.gegames.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import ge.games.gegames.entity.user.Role;
import ge.games.gegames.entity.user.User;
import ge.games.gegames.enums.UserStatusE;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FirebaseService {

    public FirebaseToken verifyToken(String token) throws FirebaseAuthException {

        return FirebaseAuth.getInstance().verifyIdToken(token);
    }

    public String getLoginFromToken(String token) {

        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);

            String login = firebaseToken.getEmail();

            if (StringUtil.isNullOrEmpty(login)){
                login = (String) firebaseToken.getClaims().get("phone_number");
            }
            return login;

        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Invalid firebase token" + e);
        }
    }

    public User createUser(String token) {

            return User.builder()
                    .id(0)
                    .nickname("User" + System.currentTimeMillis())
                    .login(getLoginFromToken(token))
                    .password("fire_base")
                    .roles(Set.of(new Role(2, "USER")))
                    .userStatusE(UserStatusE.BRONZE)
                    .build();

    }

}
