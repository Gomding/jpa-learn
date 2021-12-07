package com.learn.jpa.chapter14.repository;

import com.learn.jpa.chapter14.Member14;
import com.learn.jpa.chapter14.Team14;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Team14RepositoryTest {

    @Autowired
    private Team14Repository teamRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void collectionType() {
        Team14 team14 = new Team14();

        Team14 savedTeam14 = teamRepository.save(team14);

        System.out.println("savedTeam14 members Collection type is " + savedTeam14.getMember14s().getClass());
        System.out.println("savedTeam14 members List type is " + savedTeam14.getList().getClass());
        System.out.println("savedTeam14 members Set type is " + savedTeam14.getSet().getClass());
        System.out.println("savedTeam14 members @OrderColumn List type is " + savedTeam14.getOrderColumnList().getClass());

        assertThat(savedTeam14.getMember14s()).isInstanceOf(PersistentBag.class);
        assertThat(savedTeam14.getList()).isInstanceOf(PersistentBag.class);
        assertThat(savedTeam14.getSet()).isInstanceOf(PersistentSet.class);
        assertThat(savedTeam14.getOrderColumnList()).isInstanceOf(PersistentList.class);
    }

    @Test
    void orderBy() {
        Team14 team14 = new Team14();

        List<Member14> member14s = Arrays.asList(new Member14(null, "찰리a"),
                new Member14(null, "찰리b"),
                new Member14(null, "찰리c"),
                new Member14(null, "찰리d"),
                new Member14(null, "찰리e"));

        team14.getOrderColumnList().addAll(member14s);
        team14.getSet().addAll(member14s); // @OrderBy 적용됨

        Team14 savedTeam = teamRepository.save(team14);

        entityManager.flush();
        entityManager.clear();

        Team14 findTeam = teamRepository.findById(savedTeam.getId()).get();

        Set<Member14> set = findTeam.getSet();
        set.forEach(System.out::println);

        List<Member14> orderColumnList = findTeam.getOrderColumnList();
        orderColumnList.forEach(System.out::println);
    }
}