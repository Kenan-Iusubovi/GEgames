package ge.games.gegames.entity.user;

import ge.games.gegames.entity.user.enums.UserStatusE;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;


import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class User {

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
}
