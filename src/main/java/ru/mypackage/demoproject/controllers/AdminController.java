package ru.mypackage.demoproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.StatementsResponseDTO;
import ru.mypackage.demoproject.dto.errors.TokenErrorResponse;
import ru.mypackage.demoproject.dto.errors.UserErrorResponse;
import ru.mypackage.demoproject.dto.UsersResponseDTO;
import ru.mypackage.demoproject.services.StatementService;
import ru.mypackage.demoproject.services.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@Tag(name = "Admin controller", description = "Контроллер для пользователя с ролью 'ADMIN' ")
public class AdminController {


    private final UserService userService;
    private final StatementService statementService;

    @Autowired
    public AdminController(UserService userService, StatementService statementService) {
        this.userService = userService;
        this.statementService = statementService;
    }

    @GetMapping("/users")
    @Operation(summary = "Получение списка пользователей",
            description = "Данный метод при вызове возвращает список всех пользователей, " +
                    "зарегестрированных в этом сервисе. Возвращает пользователей с любыми ролями",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UsersResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            })
    public ResponseEntity<UsersResponseDTO> getUsers() {
        return new ResponseEntity<>(new UsersResponseDTO(userService.findAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/users/statements")
    @Operation(summary = "Получение списка отправленных заявлений пользователей",
            description = "Данный метод возвращает список отправленных заявлений пользователей по ряду критериев." +
                    "Таких как имя пользователя и тип заявления. Также, список заявлений можно получить " +
                    "с применением пагинации. Сортируются заявки по умолчанию. Для сортировки в обратном порядке " +
                    "нужно указать дополнительный параметр 'desc'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заявлений", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementsResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<StatementsResponseDTO> getSentStatements(
            @RequestParam(value = "type")
            @Parameter(description = "Тип заявления (SENT/DRAFT/ACCEPT/REJECT)", example = "SENT") String type,

            @RequestParam(value = "username", required = false)
            @Parameter(description = "Имя пользователя", example = "user_1") String username,

            @RequestParam(value = "page", required = false)
            @Parameter(description = "Номер страницы", example = "0") Integer page,

            @RequestParam(value = "desc", required = false)
            @Parameter(description = "Сортировка заявлений в обратном порядке", example = "true") boolean sortByDesc
    ) {
        StatementsResponseDTO statementsResponseDTO = new StatementsResponseDTO(
                statementService.findAllStatementsByUserAndStatementType(
                        username,
                        type,
                        page,
                        sortByDesc)
        );

        return new ResponseEntity<>(statementsResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/add/operator")
    @Operation(summary = "Назначение оператора",
            description = "Данный метод позволяет назначить оператора. Назначается оператор " +
                    "путем смены роли пользователя с 'USER' на 'OPERATOR' ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Оператор назначен", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Пользователь с таким именем не найден", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> setOperator(
            @RequestParam(value = "username")
            @Parameter(description = "Имя пользователя", example = "user_3")
            String username
    ) {
        userService.setOperator(username);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
