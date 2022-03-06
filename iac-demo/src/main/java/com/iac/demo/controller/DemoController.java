package com.iac.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/iac/demo")
public class DemoController {

    @GetMapping(value = "/greet",  produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> greet(@RequestParam(value = "name") String name) {
            return new ResponseEntity<>("Hello "+ name, HttpStatus.OK);
    }
}
