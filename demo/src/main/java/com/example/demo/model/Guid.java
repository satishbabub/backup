package com.example.demo.model;

import javax.validation.constraints.Pattern;

public class Guid extends DataType<String> {

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Alphanumeric only")
    String value;

    private Guid(String value) {
        super(value);
    }

    public static Guid guid(String value) {
        return new Guid(value);
    }
}
