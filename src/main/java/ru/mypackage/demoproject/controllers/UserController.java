package ru.mypackage.demoproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.*;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.services.StatementService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final StatementService statementService;

    @Autowired
    public UserController(StatementService statementService) {
        this.statementService = statementService;
    }

    //Метод для получения заявок поштучно
    @GetMapping("/get")
    public ResponseEntity<Statement> getOneById(@RequestParam(value = "id") Integer id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return new ResponseEntity<>(statementService.findOne(id, username), HttpStatus.OK);
    }

    //Метод для просмотра всех заявок
    //Пока слишком много кода
    @GetMapping("/get/all")
    public ResponseEntity<StatementsResponse> getAllStatements(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        StatementsResponse statementsResponse;

        if (type == null && page == null && !sortByDate)
            statementsResponse = new StatementsResponse(statementService.findAllByUsername(username));

        else if (page == null && type == null)
            statementsResponse = new StatementsResponse(statementService
                    .findAllByUsernameWithSorting(username, sortByDate, sortByDesc));

        else if (page == null)
            statementsResponse = new StatementsResponse(statementService
                    .findAllByUsernameAndType(username, StatementType.valueOf(type),
                            sortByDate, sortByDesc));

        else if (type == null)
            statementsResponse = new StatementsResponse(statementService
                    .findAllByUsernameAndPagination(username, page,
                            sortByDate, sortByDesc));

        else
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithAllParameters(username, StatementType.valueOf(type),
                            page, sortByDate, sortByDesc));


        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);
    }

    //Метод для получения статуса заявки
    @GetMapping("/check/status")
    public ResponseEntity<StatementType> checkStatus(@RequestParam(value = "id") Integer id) {

        return new ResponseEntity<>(statementService.checkStatus(id), HttpStatus.OK);
    }

    //Метод для создания заявки
    @PostMapping("/create/sent")
    public ResponseEntity<HttpStatus> createSent(@RequestBody CreateStatementDTO body) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        statementService
                .create(username, StatementType.SENT, body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Метод для создания черновика
    @PostMapping("/create/draft")
    public ResponseEntity<HttpStatus> createDraft(@RequestBody CreateStatementDTO body) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        statementService
                .create(username, StatementType.DRAFT, body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Метод для отправки заявки из черновика
    @PostMapping("/sent")
    public ResponseEntity<HttpStatus> sentDraft(@RequestParam(value = "id") Integer id) {
        statementService.sentStatementFromDrafts(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //Метод для редактирования черновика
    @PostMapping("/refactor/draft")
    public ResponseEntity<HttpStatus> refactorDraft(@RequestBody RefactorStatementDTO refStatementDTO) {
        statementService.refactor(refStatementDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
