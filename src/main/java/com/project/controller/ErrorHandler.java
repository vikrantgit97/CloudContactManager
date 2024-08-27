package com.project.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

public class ErrorHandler implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}
