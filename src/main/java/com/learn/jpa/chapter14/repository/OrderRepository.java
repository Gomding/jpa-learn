package com.learn.jpa.chapter14.repository;

import com.learn.jpa.chapter14.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    @EntityGraph(value = "Order.withMember")
//    Optional<Order> findById(Long id);

    @EntityGraph(attributePaths = "member14")
    Optional<Order> findById(Long id);
}