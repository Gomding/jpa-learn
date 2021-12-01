package com.learn.jpa.chapter09.member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExampleElementCollection {

}

@Entity
class Member3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address homeAddress;

    // 기본 값 타입인 String을 컬렉션으로 가진다.
    // 관계형 데이터베이스는 컬럼에 컬렉션을 포함할 수 없으므로 별도의 테이블을 추가한다. (OneToMany 관계와 비슷)
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOODS",
    joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    // 임베디드 타입인 Address를 컬렉션으로 가진다.
    // 관계형 데이터베이스는 컬럼에 컬렉션을 포함할 수 없으므로 별도의 테이블을 추가한다. (OneToMany 관계와 비슷)
    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();
}
