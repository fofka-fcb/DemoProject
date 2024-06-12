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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.*;
import ru.mypackage.demoproject.dto.errors.StatementErrorResponse;
import ru.mypackage.demoproject.dto.errors.TokenErrorResponse;
import ru.mypackage.demoproject.dto.responses.StatementsResponseDTO;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;
import ru.mypackage.demoproject.services.StatementService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Tag(name = "User controller", description = "Контроллер для пользователя с ролью 'USER' ")
public class UserController {

    private final StatementService statementService;

    @Autowired
    public UserController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/get")
    @Operation(summary = "Получение черновика/отправленных заявлений",
            description = "Данный метод позволяет получить заявление по его 'id'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Statement.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<Statement> getOneById(
            @RequestParam(value = "id")
            @Parameter(description = "'id' собственного заявления/черновика", example = "1")
            Integer id
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(statementService.findOne(id, username), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    @Operation(summary = "Получение списка заявлений",
            description = "Данный метод позволяет получить список всех заявлений пользователя." +
                    " Отсортировать заявления пожно по их типу. Также есть поддержка пагинации и сортировка.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список заявлений", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementsResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<StatementsResponseDTO> getAllStatements(
            @RequestParam(value = "type", required = false)
            @Parameter(description = "Тип заявления(DRAFT/SENT/ACCEPT/REJECT)", example = "DRAFT")
            String type,

            @RequestParam(value = "page", required = false)
            @Parameter(description = "Номер страницы при пагинации", example = "0")
            Integer page,

            @RequestParam(value = "desc", required = false)
            @Parameter(description = "Сортировка в обратном порядке", example = "false")
            boolean sortByDesc
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        StatementsResponseDTO statementsResponseDTO = new StatementsResponseDTO(
                statementService.findAllStatementsByUserAndStatementType(
                        username,
                        type,
                        page,
                        sortByDesc
                )
        );

        return new ResponseEntity<>(statementsResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/check/status")
    @Operation(summary = "Проверка статуса заявления",
            description = "Данный метод позволяет узнать статус своего заявление по его 'id'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Statement.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<StatementType> checkStatus(
            @RequestParam(value = "id")
            @Parameter(description = "'id' заявления", example = "3")
            Integer id
    ) {
        return new ResponseEntity<>(statementService.checkStatus(id), HttpStatus.OK);
    }

    @PostMapping("/create/sent")
    @Operation(summary = "Создание и отправка заявления",
            description = "Данный метод позволяет создать новое заявление и сразу же отправить " +
                    "его на рассмотрение.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление отправлено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> createSent(
            @RequestBody
            @Schema(description = "Сущность для создания и отправки заявления",
                    implementation = CreateStatementDTO.class)
            CreateStatementDTO body
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        statementService.create(username, StatementType.SENT, body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/create/draft")
    @Operation(summary = "Создание черновика",
            description = "Данный метод позволяет создать новый черновик",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Черновик создан", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> createDraft(
            @RequestBody
            @Schema(description = "Сущность для создания черновика",
                    implementation = CreateStatementDTO.class)
            CreateStatementDTO body
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        statementService.create(username, StatementType.DRAFT, body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sent")
    @Operation(summary = "Отправка заявления из черновика",
            description = "Данный метод позволяет создать заявление из черновика и отправить его на рассмотрение." +
                    " Для этого необходимо указать номер черновика.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявление создано и отправленно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Заявление уже отправлено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> sentDraft(
            @RequestParam(value = "id")
            @Parameter(description = "'id' заявления", example = "7")
            Integer id
    ) {
        statementService.sentStatementFromDrafts(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/refactor/draft")
    @Operation(summary = "Редактирование черновика",
            description = "Данный метод позволяет пользователю редактировать черновик",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Черновик отредактирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = HttpStatus.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Токен отозван/не авторизирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Указан неподходящий тип заявления", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StatementErrorResponse.class))
                    })
            }
    )
    public ResponseEntity<HttpStatus> refactorDraft(
            @RequestBody
            @Schema(description = "Сущность для редактирования черновика",
                    implementation = RefactorStatementDTO.class)
            RefactorStatementDTO refStatementDTO
    ) {
        statementService.refactor(refStatementDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
