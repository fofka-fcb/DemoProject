package ru.mypackage.demoproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.CreateStatementDTO;
import ru.mypackage.demoproject.responses.UserErrorResponse;
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

    @PostMapping("/sent")
    public ResponseEntity<HttpStatus> createStatementAndSent(@RequestBody CreateStatementDTO body) {
        statementService
                .createSent(body.getUsername(), body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/draft")
    public ResponseEntity<HttpStatus> createStatementDraft(@RequestBody CreateStatementDTO body) {
        statementService
                .createDraft(body.getUsername(), body.getStatement());

        return ResponseEntity.ok(HttpStatus.OK);
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
