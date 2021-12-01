package com.learn.jpa.chapter09.member;

import javax.persistence.*;

@Entity
public class MemberWithEmbedded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @Embedded Period period;
    @Embedded Address address;
}
