package com.learn.jpa.chapter14.repository;

import com.learn.jpa.chapter14.Member14;
import com.learn.jpa.chapter14.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    @Test
    void findWithMember() {
        Member14 member14 = new Member14(null, "찰리");
        
        Order order = new Order(null, member14);

        Order savedOrder = orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        Order withMember = orderRepository.findById(order.getId()).get();

        withMember.getMember14().getName();
    }
}