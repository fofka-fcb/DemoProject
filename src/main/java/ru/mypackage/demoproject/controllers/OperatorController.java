package ru.mypackage.demoproject.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operator")
@CrossOrigin("*")
public class OperatorController {

    @GetMapping("/")
    public String helloOperator() {
        return "hello Operator";
    }

}
