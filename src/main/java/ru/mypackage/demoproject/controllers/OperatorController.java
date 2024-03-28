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
import ru.mypackage.demoproject.dto.errors.StatementErrorResponse;
import ru.mypackage.demoproject.dto.StatementsResponseDTO;
import ru.mypackage.demoproject.dto.errors.TokenErrorResponse;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;
import ru.mypackage.demoproject.services.StatementService;

@RestController
@RequestMapping("/operator")
@CrossOrigin("*")
@Tag(name = "Operator controller", description = "Контроллер для пользователя с ролью 'OPERATOR' ")
public class OperatorController {

    private final StatementService statementService;

    @Autowired
    public OperatorController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/get")
    @Operation(summary = "Получение отправленного заявления",
            description = "Данный метод при вызове возвращает отправленное пользователем заявление." +
                    " Получение заявление происходит по 'id' самого заявления.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заявлений", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Statement.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено или уже обработано", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<Statement> getOneStatementById(
            @RequestParam(value = "id")
            @Parameter(description = "'id' заявления", example = "1")
            Integer id
    ) {
        return new ResponseEntity<>(statementService.findOne(id, StatementType.SENT), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    @Operation(summary = "Получение списка отправленных заявлений",
            description = "Данный метод при вызове возвращает список отправленных на рассмотрение заявлений." +
                    " Можно получить либо все отправленные заявления, либо их часть по имени конкретного " +
                    "пользователя. Поддерживается пагинация и сортировка",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заявлений", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementsResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<StatementsResponseDTO> getAllSentStatements(
            @RequestParam(value = "username", required = false)
            @Parameter(description = "Имя пользователя", example = "user_1")
            String username,

            @RequestParam(value = "page", required = false)
            @Parameter(description = "Номер страницы при пацинации", example = "0")
            Integer page,

            @RequestParam(value = "desc", required = false)
            @Parameter(description = "Сортировка в обратном порядке", example = "true")
            boolean sortByDesc
    ) {
        StatementsResponseDTO statementsResponseDTO = new StatementsResponseDTO(
                statementService.findAllStatementsByUserAndStatementType(
                        username,
                        "SENT",
                        page,
                        sortByDesc)
        );

        return new ResponseEntity<>(statementsResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/accept")
    @Operation(summary = "Изменение статуса заявления на 'Принято'",
            description = "Данный метод позволяет оператору изменить статус зявления " +
                    "с 'отправленного' на статус 'принято'. Осуществляется это по 'id' заявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление принято", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено или уже обработано", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> acceptStatement(
            @RequestParam(value = "id")
            @Parameter(description = "'id' заявления", example = "5")
            Integer id
    ) {
        statementService.acceptSentStatement(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/reject")
    @Operation(summary = "Изменение статуса заявления на 'Отклонено'",
            description = "Данный метод позволяет оператору изменить статус зявления " +
                    "с 'отправленного' на статус 'отклонено'. Осуществляется это по 'id' заявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление отклонено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено или уже обработано", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> rejectStatement(
            @RequestParam(value = "id")
            @Parameter(description = "'id' заявления", example = "6")
            Integer id
    ) {
        statementService.rejectSentStatement(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
