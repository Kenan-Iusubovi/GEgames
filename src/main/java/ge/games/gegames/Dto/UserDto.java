package ge.games.gegames.Dto;

import ge.games.gegames.entity.user.User;
import ge.games.gegames.exception.EntityMappingException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDto {

    private static final String CLASS_NAME = UserDto.class.getSimpleName();

    private long id;

    private String nickname;

    private String mail;

    private Set<RoleDto> roles;


    public static UserDto from(User user){

        checkEntity(user);

        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .mail(user.getMail())
                .roles(RoleDto.fromSet(user.getRoles()))
                .build();
    }

    private static void checkEntity(User user){

        if (user == null){
            throw EntityMappingException.noEntity(CLASS_NAME);
        }
        if (user.getId() == 0){
            throw EntityMappingException.blankField("ID", CLASS_NAME);
        }
        if (user.getNickname().isBlank()){
            throw EntityMappingException.blankField("NICKNAME", CLASS_NAME);
        }
        if (user.getMail().isBlank()){
            throw EntityMappingException.blankField("MAIL", CLASS_NAME);
        }
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw EntityMappingException.blankField("ROLES", CLASS_NAME);
        }
    }
}
