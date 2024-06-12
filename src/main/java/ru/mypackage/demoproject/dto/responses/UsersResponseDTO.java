package ru.mypackage.demoproject.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mypackage.demoproject.dto.UserDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Сущность для отправки списка зарегестрированных в сервисе пользователей")
public class UsersResponseDTO {

    @Schema(
            description = "Список пользователей",
            type = "application/json"
    )
    private List<UserDTO> userDTOList;

}
