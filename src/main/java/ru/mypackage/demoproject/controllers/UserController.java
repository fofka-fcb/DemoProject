package ru.mypackage.demoproject.controllers;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/get")
    public ResponseEntity<Statement> getOneById(@RequestParam(value = "id") Integer id,
                                                @RequestParam(value = "type") String type) {

        return new ResponseEntity<>(statementService.findOne(id, StatementType.valueOf(type)), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public StatementsResponse getAllStatements(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (page == null) {
            return new StatementsResponse(statementService.findAllByUsername(username, StatementType.valueOf(type)));
        } else {
            return new StatementsResponse(
                    statementService.findAllWithPaginationAndSort(username, StatementType.valueOf(type),
                            page, sortByDate, sortByDesc));
        }
    }

    @GetMapping("/check/status")
    public ResponseEntity<StatementType> checkStatus(@RequestParam(value = "id") Integer id) {

        return new ResponseEntity<>(statementService.checkStatus(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createStatement(@RequestBody CreateStatementDTO body) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        statementService
                .create(username, body.getStatementType(), body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/sent")
    public ResponseEntity<HttpStatus> sentDraft(@RequestParam(value = "id") Integer id) {
        statementService.sentStatementFromDrafts(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/refactor/draft")
    public ResponseEntity<HttpStatus> refactorDraft(@RequestBody RefactorStatementDTO refStatementDTO) {
        statementService.refactor(refStatementDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
