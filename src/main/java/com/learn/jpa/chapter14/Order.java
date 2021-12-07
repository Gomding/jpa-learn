package com.learn.jpa.chapter14;

import javax.persistence.*;

@NamedEntityGraph(name = "Order.withMember", attributeNodes = {
        @NamedAttributeNode("member14")
})
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MEMBER_ID")
    private Member14 member14;

    public Order() {
    }

    public Order(Long id, Member14 member14) {
        this.id = id;
        this.member14 = member14;
    }

    public Long getId() {
        return id;
    }

    public Member14 getMember14() {
        return member14;
    }
}
