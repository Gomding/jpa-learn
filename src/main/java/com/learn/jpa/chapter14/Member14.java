package com.learn.jpa.chapter14;

import javax.persistence.*;

@Entity
public class Member14 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;

    @Convert (converter = BooleanToYNConverter.class)
    private boolean vip;

    public Member14() {
    }

    public Member14(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        System.out.println("Member.prePersist id=" + this.id);
    }

    @PostPersist
    public void postPersist() {
        System.out.println("Member.postPersist id=" + this.id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Member14{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
