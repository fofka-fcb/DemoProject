package ru.mypackage.demoproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.CreateStatementDTO;
import ru.mypackage.demoproject.dto.StatementsResponse;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.dto.UserErrorResponse;
import ru.mypackage.demoproject.services.StatementService;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final StatementService statementService;

    @GetMapping("/")
    public String helloUser() {
        return "hello User";
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createStatement(@RequestBody CreateStatementDTO body) {
        statementService
                .create(body.getUsername(), body.getStatementType(), body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get")
    public StatementsResponse getAllStatements(@RequestParam(value = "username") String username,
                                           @RequestParam(value = "type") String type,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "per_page", required = false) Integer draftsPerPage,
                                           @RequestParam(value = "sort", required = false) boolean sortByDate,
                                           @RequestParam(value = "desc", required = false) boolean sortByDesc) {
        if (page == null || draftsPerPage == null) {
            return new StatementsResponse(statementService.findAll(username, StatementType.valueOf(type)));
        } else {
            return new StatementsResponse(
                    statementService.findWithPaginationAndSort(username, StatementType.valueOf(type),
                            page, draftsPerPage, sortByDate, sortByDesc));
        }
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
