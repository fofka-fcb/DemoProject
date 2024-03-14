package ru.mypackage.demoproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(UserRoleJunctionId.class)
@Table(name = "user_role_junction")
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleJunction {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_user")
    private ApplicationUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
}
