package hello.hello_spring;

import hello.hello_spring.repository.*;
import hello.hello_spring.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//직접 설정 파일을 만들고 코드를 작성하여 스프링 빈에 등록하는 방법
@Configuration
public class SpringConfig {

//    private final DataSource dataSource;
//    private final EntityManager em;

    private final MemberRepository memberRepository;

//    @Autowired
//    //스프링이 'application.properties'의 DB 정보를 읽고 DB와 연결할 수 있는 dataSource를 만들어서 주입해줌
//    //스프링이 EntityManager를 생성해서 주입해줌으로 우리는 주입 받아서 사용하면 됨
//    public SpringConfig(DataSource dataSource, EntityManager em) {
//        this.dataSource = dataSource;
//        this.em = em;
//    }

    //스프링 컨테이너에서 MemberRepository를 찾음. 이때 내가 직접 등록한 repository는 없음.(빈으로 등록하는 repository 메서드 주석처리 했기 때문)
    //그런데 SpringDataJpaMemberRepository 하나는 있음.
    //이렇게 extends JpaRepository<T, ID>, MemberRepository를 해서 SpringDataJpaMemberRepository 인터페이스만 만들어 놓으면 스프링 데이터 JPA가 인터페이스에 대한 구현체를 알아서 만들어냄. 그리고 스프링 빈에 등록해놓음.
    //그래서 우리는 이렇게 인젝션을 받을 수가 있다.
    @Autowired
    public SpringConfig(MemberRepository memberRepository) { //MemberRepository를 인젝션 받으면 스프링 데이터 JPA가 만들어놓은 SpringDataJpaMemberRepository 인터페이스의 구현체가 인젝션 되면서 스프링 빈으로 등록됨
        this.memberRepository = memberRepository;
    }

//    @Bean //스프링 빈으로 등록한다는 의미
//    public MemberService memberService() {
//        return new MemberService(memberRepository()); //memberRepository()를 호출하면 스프링 빈에 등록되어 있는 MemoryMemberRepository를 가져와서 MemberService에 넣어줌(@Autowired 동작을 수동으로 하는거라 생각하면 됨)
//    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository); //memberService에 의존 관계 세팅을 해줘야 하므로 스프링 데이터 JPA가 만들어놓은 SpringDataJpaMemberRepository 인터페이스의 구현체를 서비스 계층에 넘겨줌(등록 시켜줌)
    }

//    @Bean
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }
}
