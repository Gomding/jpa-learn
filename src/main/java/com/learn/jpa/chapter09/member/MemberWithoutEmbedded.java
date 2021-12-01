package com.learn.jpa.chapter09.member;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MemberWithoutEmbedded {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    // 근무 기간
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    // 집 주소
    private String city;
    private String street;
    private String zipcode;

    public MemberWithoutEmbedded() {
    }

    public MemberWithoutEmbedded(Long id, String name, int age, Date startDate, Date endDate, String city, String street, String zipcode) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }
}
