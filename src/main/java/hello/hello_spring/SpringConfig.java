package hello.hello_spring;

import hello.hello_spring.repository.JdbcMemberRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//직접 설정 파일을 만들고 코드를 작성하여 스프링 빈에 등록하는 방법
@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) { //스프링이 'application.properties'의 DB 정보를 읽고 DB와 연결할 수 있는 dataSource를 만들어서 주입해줌
        this.dataSource = dataSource;
    }

    @Bean //스프링 빈으로 등록한다는 의미
    public MemberService memberService() {
        return new MemberService(memberRepository()); //memberRepository()를 호출하면 스프링 빈에 등록되어 있는 MemoryMemberRepository를 가져와서 MemberService에 넣어줌(@Autowired 동작을 수동으로 하는거라 생각하면 됨)
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        return new JdbcMemberRepository(dataSource);
    }
}
