package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//스프링 데이터 JPA는 JpaRepository<T, ID>를 받아야함 (인터페이스가 인터페이스를 받을 때는 extends, 인터페이스는 다중 상속 가능)
//JpaRepository를 받고 있으면 스프링 데이터 JPA가 SpringDataJpaMemberRepository 인터페이스를 보고 SpringDataJpaMemberRepository의 구현체를 알아서 만들고, 스프링 빈에 자동 등록까지 해줌. 우리는 그걸 가져다 쓰면 됨
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository { //T: 관리할 엔티티의 타입, ID: 해당 엔티티의 PK 타입
    //다른 메서드들을 오버라이딩 안하는 이유는 스프링 데이터 JPA에서 제공하는 JpaRepository 안에 다 들어있기 때문
    //JpaRepository 들어가보면 CRUD 해주는 리포지토리나 페이징 처리 해주는 리포지토리 등 기본적인 기능을 제공해줌
    @Override
    Optional<Member> findByName(String name); //이거는 스프링 데이터 JPA가 제공하는 기능에 없음. 그래서 메서드를 만든 것.
    //이렇게 위와 같이 메서드를 명시해주면 스프링 데이터 JPA가 findBy의 Name이라는 키워드를 보고(스프링 데이터 JPA가 보는 규칙이 있음),
    //"select m from Member m where m.name = ?"이라는 JPQL 쿼리문을 짜면 이 쿼리가 SQL로 번역이 되서 실행됨

}
