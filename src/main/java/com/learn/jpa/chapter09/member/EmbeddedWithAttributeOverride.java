package com.learn.jpa.chapter09.member;

import javax.persistence.*;

@Entity
public class EmbeddedWithAttributeOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded Address homeAddress;

    @AttributeOverrides({
            @AttributeOverride(name="city", column = @Column(name="COMPANY_CITY")),
            @AttributeOverride(name="street", column = @Column(name="COMPANY_STREET")),
            @AttributeOverride(name="zipcode", column = @Column(name="COMPANY_ZIPCODE"))
    })
    @Embedded
    Address companyAddress;
}
