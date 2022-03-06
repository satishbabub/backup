package com.example.demo.controller;

import com.example.demo.model.Guid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = {"/mytest"}, consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<Object> appReimport(@RequestBody @NotEmpty List<@Valid Guid> guids) {
        return new ResponseEntity<>("{\"message\":\"" + "test" + "\"}", HttpStatus.OK);
    }
}
