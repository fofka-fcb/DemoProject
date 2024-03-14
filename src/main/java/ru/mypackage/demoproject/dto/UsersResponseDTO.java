package ru.mypackage.demoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsersResponseDTO {

    private List<UserDTO> userDTOList;

}
