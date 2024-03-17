package ru.mypackage.demoproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mypackage.demoproject.dto.StatementsResponse;
import ru.mypackage.demoproject.dto.UsersResponseDTO;
import ru.mypackage.demoproject.models.StatementType;
import ru.mypackage.demoproject.services.StatementService;
import ru.mypackage.demoproject.services.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final UserService userService;
    private final StatementService statementService;

    @Autowired
    public AdminController(UserService userService, StatementService statementService) {
        this.userService = userService;
        this.statementService = statementService;
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponseDTO> getUsers() {
        return new ResponseEntity<>(new UsersResponseDTO(userService.findAllUsers()), HttpStatus.OK);
    }

    @GetMapping("/users/statements")
    public ResponseEntity<StatementsResponse> getSentStatements(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) boolean sortByDate,
            @RequestParam(value = "desc", required = false) boolean sortByDesc
    ) {
        StatementsResponse statementsResponse;

        if (username == null && page == null)
            statementsResponse = new StatementsResponse(statementService
                    .findAllStatementsByType(StatementType.valueOf(type), page,
                            sortByDate, sortByDesc));

        else if (page == null)
            statementsResponse = new StatementsResponse(statementService
                    .findAllByUsernameAndType(username, StatementType.valueOf(type),
                            sortByDate, sortByDesc));

        else
            statementsResponse = new StatementsResponse(statementService
                    .findAllWithAllParameters(username, StatementType.valueOf(type),
                            page, sortByDate, sortByDesc));


        return new ResponseEntity<>(statementsResponse, HttpStatus.OK);
    }

    @PostMapping("/add/operator")
    public ResponseEntity<HttpStatus> setOperator(
            @RequestParam(value = "username") String username) {

        userService.setOperator(username);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
