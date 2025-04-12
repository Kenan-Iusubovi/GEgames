package ge.games.gegames.entity.user;

import ge.games.gegames.Dto.user.responce.UserDto;
import ge.games.gegames.Dto.user.request.UserRegistrationDto;
import ge.games.gegames.enums.UserStatusE;
import ge.games.gegames.exception.EntityMappingException;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;


import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    private static final String CLASS_NAME = User.class.getSimpleName();

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(name = "status")
    private UserStatusE userStatusE;


    public static User registration(UserRegistrationDto dto){

        return User.builder()
                .id(0)
                .nickname(dto.getNickname())
                .mail(dto.getMail())
                .password(dto.getPassword())
                .userStatusE(UserStatusE.NOT_CONFIRMED)
                .roles(Rol)
    }

    public static User from(UserDto dto){

        checkEntity(dto);

        return User.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .mail(dto.getMail())
                .roles(Role.setFrom(dto.getRoles()))
                .userStatusE(dto.getUserStatusE())
                .build();
    }

    private static void checkEntity(UserDto dto){

        if (dto == null){
            throw EntityMappingException.noEntity(CLASS_NAME);
        }
        if (dto.getId() == 0){
            throw EntityMappingException.blankField("ID", CLASS_NAME);
        }
        if (dto.getNickname().isBlank()){
            throw EntityMappingException.blankField("NICKNAME", CLASS_NAME);
        }
        if (dto.getMail().isBlank()){
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
