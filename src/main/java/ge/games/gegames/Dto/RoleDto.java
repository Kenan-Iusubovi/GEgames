package ge.games.gegames.Dto;

import ge.games.gegames.entity.user.Role;
import ge.games.gegames.exception.EntityMappingException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Builder
public class RoleDto {

    private static final String CLASS_NAME = RoleDto.class.getSimpleName();

    private int id;

    private String name;



    public static RoleDto from(Role role){

        checkEntity(role);

       return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Set<RoleDto> setFrom(Set<Role> roles){

        if(roles == null || roles.isEmpty()){
            return null;
        }

        return roles.stream()
                .map(RoleDto::from)
                .collect(Collectors.toSet());
    }

    private static void checkEntity(Role role){

        if (role == null){
            throw EntityMappingException.noEntity(CLASS_NAME);
        }
        if (role.getId() == 0){
            throw EntityMappingException.blankField("ID", CLASS_NAME);
        }
        if (role.getName().isBlank()){
            throw EntityMappingException.blankField("NAME", CLASS_NAME);
        }
    }
}
