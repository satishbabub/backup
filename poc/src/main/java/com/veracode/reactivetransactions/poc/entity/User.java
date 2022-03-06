package com.veracode.reactivetransactions.poc.entity;

import lombok.Data;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;
}
