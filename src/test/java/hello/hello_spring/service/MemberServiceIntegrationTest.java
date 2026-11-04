package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //실제 스프링을 띄워서 테스트 함(스프링 환경에서 테스트할 때는 이렇게 적어주면 됨)
@Transactional //이 애노테이션을 테스트 케이스에 달면 테스트를 실행할 때 트랜잭션을 먼저 실행하고, DB에 데이터 insert, select, update, delete 쿼리 등을 실행한 후 테스트가 끝나면 롤백을 해줌(테스트 데이터가 최종적으로 DB에 반영이 안됨. 이렇게 반복적으로 테스트가 가능하도록 해줌. 테스트 각각 한 건마다 적용)
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService; //테스트는 제일 끝단에 있기 때문에 테스트 코드 생성 시에는 제일 편한 방법을 씀. 그래서 필드 주입으로 함
    @Autowired MemberRepository memberRepository; //SpringConfig에서 빈으로 등록된 구현체인 JdbcMemberRepository가 들어옴

    @Test
    void 회원가입() {
        //given-when-then 패턴: 테스트 할 때 이렇게 세부분으로 나눠서 작성하면 도움이 됨
        //given - 이러한 상황이 주어져서 (기반으로 하는 데이터)
        Member member = new Member();
        member.setName("spring");

        //when - 이거를 실행했을 때 (검증하는 데이터)
        Long saveId = memberService.join(member);

        //then - 결과가 이게 나와야함 (결과로 나와야하는 데이터)
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        //왼쪽: 발생해야 하는(기대하는) 예외 클래스, 오른쪽: 해당 예외가 발생할 로직을 람다식으로 표현
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}