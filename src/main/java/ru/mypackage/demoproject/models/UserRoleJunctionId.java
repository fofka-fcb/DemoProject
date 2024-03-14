package ru.mypackage.demoproject.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleJunctionId implements Serializable {

    private ApplicationUser user;
    private Role role;

}
