# 9장. 값 타입

값 타입 : int, Integer, String 처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체를 의미
* 회원 엔티티는 키나 나이등을 변경해도 식별자만 유지되면 같은 회원으로 인식한다.
* 숫자라는 값 100을 200으로 변경하면 완전히 다른 값으로 인식한다. 단순한 수치이다.

값 타입은 3가지
* 기본 값 타입 (basic value type)
  * 자바 기본 타입(int, double)
  * 래퍼 클래스(Integer)
  * String
* 임베디드 타입 (embedded type)
* 컬렉션 값 타입 (collection value type)

엔티티는 id 라는 식별자를 가지고 생명주기도 있지만    
값 타입은 식별자 값도 없고 생명주기도 엔티티에 의존한다.

### 기본 값 타입

```java
@Entity
public class Member {
  @Id @GeneratedValue
  private Long Id;
  
  // 기본 값 타입인 name 과 age
  private String name;
  private int age;
}
```

> 값 타입은 공유되면 안된다. 다른 회원의 이름이 변경된다고 나의 이름까지 변경되는 서비스는 이상할 것이다.
> 
> 자바에서 int, double같은 기본 타입(primitive type)은 절대 공유되지 않는다.   
> a=b 코드에서 b의 값을 복사해서 a에 입력한다. 주소값 복사가 아닌 값 자체를 복사하는 것이다.

### 임베디드 타입

```java
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
```

위 엔티티는 이렇게 설명할 수 있다.
> 회원 엔티티는 이름, 나이, 근무 시작일, 근무 종료일, 주소 도시, 주소 번지, 주소 우편번호를 가진다.

단순히 정보를 풀어둔 것이다. 근무 시작일은 우편번호와 아무런 관련이 없으므로 **응집도가 떨어지고 있다.**   
다음 처럼 설명하는 것이 더 명확하다.
> 회원 엔티티는 이름, 나이, 근무 기간, 집 주소를 가진다.

```java
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @Embedded Period period;
    @Embedded Address address;
}
```

```java
@Embeddable
public class Address {

    @Column
    private String city;
    private String street;
    private String zipcode;
    
    ...
}
```

```java
@Embeddable
public class Period {

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
  
    // 값 타입에 대한 메서드도 만들 수 있다. -> 응집도 상승
    // 단일 책임의 원칙도 지킬 수 있다고 생각
    public boolean isWork(Date date) {
      return date.before(endDate);
    }
}
```

@Embeddeable : 값 타입을 정의하는 곳에 표시
@Embedded : 값 타입을 사용하는 곳에 표시

임베디드 타입은 기본 생성자가 필수   
엔티티와 임베디드 타입의 관계를 UML로 표현하면 컴포지션 관계가 된다.

> 하이버네이트는 임베디드 타입을 컴포넌트(components)라 한다.

임베디드 타입은 엔티티의 값일 뿐이다. 따라서 값이 속한 엔티티의 테이블에 매핑한다.   
즉, 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.

**잘 설계한 ORM 애플리케이션은 매피;ㅇ한 테이블의 수보다 클래스의 수가 더 많다.**

ORM을 사용하지 않고 개발하면 테이블 컬럼과 객체 필드를 대부분 1:1로 매핑한다.
SQL을 직접 다루면 테이블하나에 클래스 하나를 매핑하는 것도 힘든데 여러개의 클래스를 매핑하는건 더더욱 힘들것이다.

임베디드 타입은 값 타입을 포함하거나 엔티티를 참조할 수 있다.
> 엔티티는 공유될 수 있으므로 **참조** 한다고 표현하고,   
> 값 타입은 특정 주인에 소속되고 논리적인 개념상 공유되지 않으므로 포함한다고 표현한다.

```java
@Embeddedable
public class PhoneNumber {
    String areaCode;
    String localNumber;
    @ManyToOne
    PhoneServicePrivider provider; //엔티티 참조
}
```

임베디드 타입에 정의한 매핑 정보를 재정의 하려면 엔티티에 @AttributeOverride를 사용하면 된다.
테이블에 매핑하는 컬럼명이 중복될 때 활용할 수 있다.
```java
@Entity
public class EmbeddedWithAttributeOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Embedded
    Address homeAddress;
  
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "COMPANY_ZIPCODE"))
    })
    @Embedded
    Address companyAddress;
}
```
@AttributeOverride를 사용하면 어노테이션을 너무 많이 사용해서 엔티티 코드가 지저분해진다.   
-> 다행히 하나의 엔티티에 같은 임베디드 타입을 중복해서 사용하는 일은 많지 않다.
> 임베디드 타입이 null이면 매핑된 컬럼 값은 모두 null이 된다.

### 값 타입과 불변 객체
값 타입은 단순하고 안전하게 다룰 수 있어야한다.   
임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다.   
위에서 말했던 다른 사람의 회원 정보가 변경된 것이 나의 정보에도 영향을 미칠 수 있게된다.

```java
member1.setHomeAddress(new Address("Old City"));
Address address = member1.getHomeAddress();

// 회원 2의 집 주소를 변경하려고 했지만 해당 코드는 회원 1의 집 주소도 함께 변경된다.
address.setCity("New City");
member2.setHomeAddress(address);
```
이렇듯 뭔가를 수정했는데 전혀 예상치 못한 곳에서 문제가 발생하는 것을 부작용(side-effect)이라 한다.

이런 부작용을 막기 위해 값 타입을 복사해서 사용하는 방법이 있다.
```java
member1.setHomeAddress(new Address("Old City"));
Address address = member1.getHomeAddress();

Address newAddress = address.clone();

// 복사된 값 객체의 주소를 변경했으므로 회원2의 정보만 변경된다.
newAddress.setCity("New City");
member2.setHomeAddress(newAddress);
```

항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용은 피할 수 있다.   
하지만 임베디드 타입처럼 직접 정의한 값 타임은 자바의 기본 타입(primitive type)이 아니라 객체 타입이라는 것이다.   
Address같은 객체 타입을 복사해서 넘기는것으로 공유 참조의 부작용은 막았지만 이를 모르는 개발자의 실수로 인해 복사하지 않고 원본의 참조값을 직접 넘기는 것을 막을 방법이 없다.   
(원본 참조 값을 직접 넘기는 것을 불가능하게 만드는 방법이 없다.)

#### 불변 객체
따라서 근본적인 해결책으로 단순한 방법은 객체의 값을 수정하지 못하게 막으면 된다.   
값 타입은 setter 같이 내부의 필드를 변경할 수 없게 구현하여 불변하게 만들어 주는것이 좋다.   
객체를 불변하게 만들면 값을 수정할 수 없으므로 부작용을 원천 차단할 수 있다.   
값 타입은 될 수 있으면 불변 객체로 설계해야한다.

> 한 번 만들면 절대 변경할 수 없는 객체를 불변 객체라 한다.

불변객체도 객체이므로 인스턴스 참조 값 공유는 피할 수 없다. 하지만 참조 값을 공유해도 인스턴스의 값을 수정할 수 없으므로 부작용이 발생하지 않는다.

```java
@Embeddable
public class Address {
    
    private String city;
    
    protected Address() {}
  
    // 생성자로 초기 값을 설정한다.
    public Address(String city) {
        this.city = city;
    }
    
    // getter는 노출한다.
    public String getCity() {
        return this.city;
    }
    
    // setter는 만들지 않는다.
}
```

### 값 타입의 비교

```java
int a = 10;
int b = 10;

Address a = new Address("서울시", "송파구", "4-3/4번지");
Address b = new Address("서울시", "송파구", "4-3/4번지");
```

* int a 숫자 10과 int b 숫자 10은 같다고 표현한다.
* Address a 와 Address b는 값이 같으므로 같다고 표현한다.

자바의 객체 비교 2가지
* 동일성(identity) 비교 : 인스턴스의 참조 값을 비교, == 사용
* 동등성(Equivalence) 비교 : 인스턴스의 값을 비교, equals() 사용

Address 값 타입을 'a == b' 로 비교하면 둘은 다른 인스턴스이므로 거짓이다.   
하지만 값 타입은 비록 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야한다.   
따라서 값 타입 비교에는 a.equals(b) 를 사용해야 한다. (물론 equals 메서드 재정의는 필수다)

값 타입의 equals() 메서드를 재정의할 때는 보통 모든 필드의 값을 비교하도록 구현한다.

### 값 타입 컬렉션

하나 이상의 값을 저장하려면 컬렉션을 지정하고 @ElementCollection, @CollectionTable 어노테이션을 사용하면 된다.

```java
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
```

관계형 데이터베이스는 컬럼에 컬렉션을 포함할 수 없으므로 별도의 테이블을 추가한다. (OneToMany 관계와 비슷)   

테이블 매핑 정보는 @AttributeOverride를 사용해서 재정의 할 수 있다.

> 만약 @CollectionTable 어노테이션을 생략하면 기본값을 사용해서 매핑한다.   
> 기본값 : {엔티티이름}_{컬렉션 속성 이름}   
> addressHistory의 경우 Member_addressHistory 테이블과 매핑한다.

등록(insert), 조회(select)의 경우 값 타입 컬렉션은 OneToMany와 유사하게 동작한다.
* 값 타입 컬렉션은 영속성 전이(Cascade) + 고아 객체 제거(ORPHAN REMOVE) 기능을 필수로 가진다고 볼 수 있다.
* 값 타입 컬렉션도 조회할 때 패치 전략을 선택할 수 있다.(LAZY가 기본이다.)
  * @ElementCollection(fetch = FetchType.LAZY)

수정 시는 약간의 차이점을 알아둬야 한다.

```java
Member member = em.find(Member.class, 1L);

// 1. 임베디드 값 타입 수정
member.setHomeAddress(new Address("새로운 도시", "신도시1", "1234번지"));

// 2. 기본값 타입 컬렉션 수정
Set<String> favoriteFoods = member.getFavoriteFoods();
favoriteFoods.remove("탕수육");
favoriteFoods.add("치킨");

// 3. 임베디드 값 타입 컬렉션 수정
List<Address> addressHistory = member.getAddressHistory();
addressHistory.remove(new Address("서울", "기존 주소", "123-123"));
addressHistory.add(new Address("새로운 도시", "새로운 주소", "1234번지"));
```

* **임베디드 값 타입 수정** : homeAddress 임베디드 값 타입은 MEMBER 테이블과 매핑했으므로 MEMBER 테이블만 UPDATE한다. 사실 Member 엔티티를 수정하는 것과 같다.
* **기본 값 타입 컬렉션 수정** : 탕수육을 치킨으로 변경하려면 탕수육을 제거하고 치킨을 추가해야 한다. 자바의 String 타입은 수정할 수 없다.
* **임베디드 값 타입 컬렉션 수정** : 값 타입은 불변해야 한다. 따라서 컬렉션에서 기존 주소를 삭제하고 새로운 주소를 등록했다. 참고로 값 타입은 equals, hashCode를 꼭 구현해야한다.

#### 값 타입 컬렉션의 제약사항

특정 엔티티 하나에 소속된 값 타입은 값이 변경되어도 자신이 소속된 엔티티를 데이터베이스에서 찾고 값을 변경하면 된다. 문제는 값 타입 컬렉션이다.   
1. 값 타입 컬렉션에 보관된 값 타입들은 별도의 테이블에 보관된다.
2. 여기에 보관된 값 타입의 값이 변경되면 데이터베이스에 있는 원본 데이터를 찾기 어렵다는 문제가 있다.
```java
new Address("서울", "기존 주소", "123-123");
new Address("서울", "기존 주소", "321-321");

// member의 ID가 100이라면

INSERT INTO ADDRESS(MEMBER_ID, CITY, STREET, ZIPCODE) VALUES(100, "서울", "기존 주소", "123-123");
INSERT INTO ADDRESS(MEMBER_ID, CITY, STREET, ZIPCODE) VALUES(100, "서울", "기존 주소", "321-321");
```
위 예시를 보면 데이터베이스에서는 특정 값을 변경했을때 어떤 레코드를 변경해야할지 찾는 과정이 까다롭다는게 느껴질 것이다.
각각의 레코드에 모든 값을 비교하며 모든 값이 일치하는 레코드를 찾아내야한다.

이런 문제로 인해 JPA 구현체들은 값 타입 컬렉션에 변경 사항이 발생하면, 
값 타입 컬렉션이 매핑된 테이블의 연관된 모든 데이터를 삭제하고, 
현재 값 타입 컬렉션 객체에 있는 모든 값을 데이터베이스에 다시 저장한다.

만약 address 컬렉션에 100개의 값이 있다면 하나의 값을 변경해도 100개의 데이터를 삭제하고 100개의 데이터를 다시 저장할 것이다.

> 따라서 실무에서는 값 타입 컬렉션이 매핑된 테이블에 데이터가 많다면 값 타입 컬렉션 대신에 일대다 관계를 고려해야 한다.   
> 추가로 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야한다.

값 타입 컬렉션을 변경했을 때 JPA 구현체들은 테이블의 기본 키를 식별해서 변경된 내용만 반영하려고 노력한다.   
하지만 사용하는 컬렉션이나 여러 조건에 따라 기본키를 식별할 수도 있고 식별하지 못할 수도 있다. 따라서 값 타입 컬렉션을 사용할 때는 모두 삭제하고 다시 저장하는 최악의 시나리오를 고려하면서 사용해야 한다.

### 값 타입의 특징 정리

* 식별자가 없다.
* 생명 주기를 엔티티에 의존한다.
  * 스스로 생명주기를 가지지 않고 엔티티에 의존한다. 의존하는 엔티티를 제거하면 같이 제거된다.
* 공유하지 않는 것이 안전하다
  * 엔티티 타입과는 다르게 공유하지 않는것이 안전하다. 대신에 값을 복사해서 사용해야한다.
  * 오직 하나의 주인만이 관리해야한다.
  * 불변 객체로 만드는 것이 안전하다.
* 값 타입은 정말 값 타입이라 판단될 때만 사용해야 한다. 특히 엔티티와 값 타입을 혼돈해서 엔티티를 값 타입으로 만들면 안 된다.

> 식별자가 필요하고 지속해서 값을 추적하고 구분하고 변경해야 한다면 그것은 값 타입이 아닌 엔티티다.