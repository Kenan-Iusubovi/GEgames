package ge.games.gegames.initializer;


import ge.games.gegames.entity.user.Role;
import ge.games.gegames.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository repository;


    @PostConstruct
    public void init(){
        createRoleIfNotFound(1, "ADMIN");
        createRoleIfNotFound(2, "USER");
        createRoleIfNotFound(3, "VIP");
    }

    private void createRoleIfNotFound(int id, String name){

        repository.findById(id).ifPresentOrElse(
                role -> {
                    if (!role.getName().equals(name)){
                        role.setName(name);
                        repository.save(role);
                    }
                },
                () -> {
                    Role newRole = Role.builder()
                            .id(id)
                            .name(name)
                            .build();
                    repository.save(newRole);
                }
        );
    }
}
