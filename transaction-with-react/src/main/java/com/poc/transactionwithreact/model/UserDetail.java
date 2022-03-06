package com.poc.transactionwithreact.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class UserDetail {
    @Id
    private Integer id;

    @Column
    private String name;

    @Column
    private String role;
}
