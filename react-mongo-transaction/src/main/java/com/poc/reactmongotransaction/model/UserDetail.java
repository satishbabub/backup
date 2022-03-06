package com.poc.reactmongotransaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserDetail {
    @Id
    private BigInteger id;

    private String name;

    private String role;
}
