package ge.games.gegames.entity.user;

import ge.games.gegames.Dto.RoleDto;
import ge.games.gegames.exception.EntityMappingException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

    private static final String CLASS_NAME = Role.class.getSimpleName();

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;


    public static Role from(RoleDto role){

        checkEntity(role);

        return Role.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Set<Role> setFrom(Set<RoleDto> roles){

        if(roles == null || roles.isEmpty()){
            return null;
        }

        return roles.stream()
                .map(Role::from)
                .collect(Collectors.toSet());
    }

    private static void checkEntity(RoleDto role){

        if (role == null){
            throw EntityMappingException.noEntity(CLASS_NAME);
        }
        if (role.getId() < 0){
            throw EntityMappingException.blankField("ID", CLASS_NAME);
        }
        if (role.getName().isBlank()){
            throw EntityMappingException.blankField("NAME", CLASS_NAME);
        }
    }
}
