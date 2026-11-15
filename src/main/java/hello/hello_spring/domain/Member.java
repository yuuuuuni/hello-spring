package hello.hello_spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //이 클래스가 DB 테이블과 매핑되는 엔티티임을 JPA에게 알려주는 어노테이션
//이 어노테이션을 붙이면 JPA가 해당 클래스를 관리하게 됨(Member를 JPA가 관리하는 엔티티라고 표현)
public class Member {

    @Id //id 필드 위에 @Id를 달아서 PK를 매핑해 줘야 함(id 필드가 PK라고 JPA에게 알려주는 것)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //DB에 데이터를 삽입하면 DB가 id를 자동으로 생성해 주는데, 이걸 IDENTITY 전략이라고 함
    //@GeneratedValue: PK를 자동으로 생성하도록 지정하는 어노테이션
    //strategy = GenerationType.IDENTITY: PK를 자동 생성하는 여러 방식 중 하나로, DB의 자동 증가 기능을 이용하는 방식
    private Long id; //식별번호
    private String name; //이름

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
