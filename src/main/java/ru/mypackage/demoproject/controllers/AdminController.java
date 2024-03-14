package ru.mypackage.demoproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.StatementErrorResponse;
import ru.mypackage.demoproject.dto.StatementsResponse;
import ru.mypackage.demoproject.dto.UserErrorResponse;
import ru.mypackage.demoproject.dto.UsersResponseDTO;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.services.StatementService;
import ru.mypackage.demoproject.services.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final StatementService statementService;

    @GetMapping("/users")
    public ResponseEntity<UsersResponseDTO> getUsers() {
        return new ResponseEntity<>(new UsersResponseDTO(userService.findAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/users/sent")
    public ResponseEntity<StatementsResponse> getSentStatements(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc
    ) {
        StatementsResponse statementsResponse;

        if (username == null) {
            statementsResponse = new StatementsResponse(statementService.findAllStatementsByType(StatementType.SENT));

        } else {
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithPaginationAndSort(username, StatementType.SENT, page, sortByDate, sortByDesc
                    ));
        }

        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);
    }

    @GetMapping("/users/accept")
    public ResponseEntity<StatementsResponse> getAcceptStatements(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc) {

        StatementsResponse statementsResponse;

        if (username == null) {
            statementsResponse = new StatementsResponse(statementService.findAllStatementsByType(StatementType.ACCEPT));

        } else {
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithPaginationAndSort(username, StatementType.ACCEPT, page, sortByDate, sortByDesc
                    ));
        }

        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);

    }

    @GetMapping("/users/reject")
    public ResponseEntity<StatementsResponse> getRejectStatements(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc) {

        StatementsResponse statementsResponse;

        if (username == null) {
            statementsResponse = new StatementsResponse(statementService.findAllStatementsByType(StatementType.REJECT));

        } else {
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithPaginationAndSort(username, StatementType.REJECT, page, sortByDate, sortByDesc
                    ));
        }

        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);
    }

    @PostMapping("/add/operator")
    public ResponseEntity<HttpStatus> setOperator(
            @RequestParam(value = "username") String username) {

        userService.setOperator(username);
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
