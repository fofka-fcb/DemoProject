package ru.mypackage.demoproject.services;

import ru.mypackage.demoproject.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAllUsers();

    void setOperator(String username);

}
