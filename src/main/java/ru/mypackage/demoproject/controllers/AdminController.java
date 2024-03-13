package ru.mypackage.demoproject.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @GetMapping("/")
    public String helloAdmin() {
        return "hello Admin";
    }

}
