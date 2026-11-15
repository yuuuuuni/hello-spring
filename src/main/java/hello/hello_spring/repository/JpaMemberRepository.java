package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    //JPA는 EntityManager로 모든 것이 동작함. 따라서 EntityManager를 선언하면 됨
    //build.gradle에서 data-jpa 라이브러리를 받았으면 스프링 부트가 자동으로 EntityManager를 생성해줌(현재 데이터베이스와 연결까지 다 해줌)
    //(application.properties의 정보와 DB 커넥션 정보 등을 다 자동으로 모아서 스프링 부트가 EntityManager를 만들어줌. 얘는 내부적으로 Datasource를 들고 있어서 DB랑 통신하는 것을 내부에서 다 처리함)
    //우리는 이렇게 만들어진 EntityManager를 주입 받으면 됨
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) { //스프링 부트가 알아서 EntityManager를 주입해줌
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member); //persist의 뜻이 '영속하다', '영구 저장하다'의 의미. 이렇게 하면 JPA가 알아서 insert 쿼리 만들어서 DB에 저장하고 그때 생성되는 id 값도 member에 setId로 해서 다 넣어줌.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);//find()라는 메서드가 있음. 조회할 타입이 Member이므로 Member.class 넣어주고 id로 찾을거니까 식별자(PK)인 id 넣어주면 됨.(PK로 조회하는 경우에는 이렇게 조회하면 됨) 이렇게 하면 select 쿼리가 만들어져서 실행되고 조회가 됨!
        return Optional.ofNullable(member); //반환 타입이 Optional<>이고 조회된 값이 없을 수도 있기 때문에 Optional.ofNullable()로 감싸서 반환
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class) //':name'은 파라미터 이름. 나중에 실제 값을 넣을 자리
                .setParameter("name", name) //':name' 파라미터에 매개변수로 넘어온 name을 넣어주겠다.
                .getResultList(); //조회 결과를 List로 반환

        return result.stream().findAny(); //List를 Stream으로 변환한 후 요소 하나를 찾아 Optional로 반환함 (findAny()를 사용하면 자동으로 Optional로 반환됨)
    }

    @Override
    public List<Member> findAll() {
        //JPQL은 객체 지향 쿼리 언어로, DB 테이블이 아닌 객체(정확히는 엔티티)를 대상으로 쿼리를 실행함. 그러면 JPA가 JPQL을 SQL로 변환하여 DB에 전달함.
        //"Member m"에서 Member는 엔티티를 가리키고, m은 Member 엔티티에 붙인 별칭(Alias)임.
        //"select m"은 별칭 m(즉, Member 엔티티 객체)을 조회하겠다는 뜻
        //두번째의 Member.class는 조회할 타입
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); //쿼리 실행 결과를 List 형태로 반환함
    }
}
