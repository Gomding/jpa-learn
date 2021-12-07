package com.learn.jpa.chapter14;

import javax.persistence.*;
import java.util.*;

@Entity
@EntityListeners(Team14Listener.class)
public class Team14 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn
    private Collection<Member14> member14s = new ArrayList<>();

    @OneToMany
    @JoinColumn
    private List<Member14> list = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @OrderBy("name desc, id asc")
    @JoinColumn
    private Set<Member14> set = new HashSet<>();

    // @OrderColumn은 순서를 보장하는 컬럼을 추가한다 (다 쪽의 테이블에 순서컬럼이 생성됨)
    // 즉 순서를 보장하는 리스트이다.
    @OneToMany
    @OrderColumn
    @JoinColumn
    private List<Member14> orderColumnList = new ArrayList<>();

    public Team14() {
    }

    public Team14(Long id, Collection<Member14> member14s) {
        this.id = id;
        this.member14s = member14s;
    }

    public Long getId() {
        return id;
    }

    public Collection<Member14> getMember14s() {
        return member14s;
    }

    public List<Member14> getList() {
        return list;
    }

    public Set<Member14> getSet() {
        return set;
    }

    public List<Member14> getOrderColumnList() {
        return orderColumnList;
    }
}

class Team14Listener {
    @PrePersist
    private void prePersist(Object obj) {
        System.out.println("Team14Listener.prePersist obj = " + obj);
    }

    @PostPersist
    private void postPersist(Object obj) {
        System.out.println("Team14Listener.postPersist obj = " + obj);
    }
}
