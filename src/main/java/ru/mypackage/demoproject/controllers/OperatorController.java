package ru.mypackage.demoproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.StatementErrorResponse;
import ru.mypackage.demoproject.dto.StatementsResponse;
import ru.mypackage.demoproject.dto.UserErrorResponse;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.services.StatementService;

@RestController
@RequestMapping("/operator")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OperatorController {

    private final StatementService statementService;

    @GetMapping("/get")
    public ResponseEntity<Statement> getOneStatementById(@RequestParam(value = "id") Integer id) {
        return new ResponseEntity<>(statementService.findOne(id, StatementType.SENT), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<StatementsResponse> getAllSentStatements(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc) {

        StatementsResponse statementsResponse;

        if (username == null) {
            statementsResponse = new StatementsResponse(statementService.findAllSentStatements());

        } else {
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithPaginationAndSort(username, StatementType.SENT, page, sortByDate, sortByDesc
                    ));
        }

        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<HttpStatus> acceptStatement(@RequestParam(value = "id") Integer id) {
        statementService.acceptSentStatement(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/reject")
    public ResponseEntity<HttpStatus> rejectStatement(@RequestParam(value = "id") Integer id) {
        statementService.rejectSentStatement(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<StatementErrorResponse> handleException(StatementNotFoundException e) {
        StatementErrorResponse response = new StatementErrorResponse(
                "Statement with this 'id' wasn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UsernameNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "Username is not valid!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
