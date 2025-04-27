package ge.games.gegames.entity.user;

import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.enums.UserStatusE;
import ge.games.gegames.exception.EntityMappingException;
import io.netty.util.internal.StringUtil;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;


import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    private static final String CLASS_NAME = User.class.getSimpleName();

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatusE userStatusE;


    public static User registration(UserRegistrationDto dto){

        return User.builder()
                .id(0)
                .nickname(dto.getNickname())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .roles(Set.of(new Role(2, "USER")))
                .userStatusE(UserStatusE.NOT_CONFIRMED)
                .build();
    }

    public static User from(UserDto dto){

        checkEntity(dto);

        return User.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .login(dto.getLogin())
                .roles(Role.setFrom(dto.getRoles()))
                .userStatusE(dto.getUserStatusE())
                .build();
    }

    private static void checkEntity(UserDto dto){

        if (dto == null){
            throw EntityMappingException.noEntity(CLASS_NAME);
        }
        if (dto.getId() < 0){
            throw EntityMappingException.blankField("ID", CLASS_NAME);
        }
        if (StringUtil.isNullOrEmpty(dto.getNickname())){
            throw EntityMappingException.blankField("NICKNAME", CLASS_NAME);
        }
        if (StringUtil.isNullOrEmpty(dto.getLogin())){
            throw EntityMappingException.blankField("MAIL", CLASS_NAME);
        }
        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            throw EntityMappingException.blankField("ROLES", CLASS_NAME);
        }
        if (dto.getUserStatusE() == null) {
            throw EntityMappingException.blankField("USER_STATUS", CLASS_NAME);
        }
    }
}
