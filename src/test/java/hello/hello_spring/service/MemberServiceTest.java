package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach //테스트 메서드들이 각각 실행되기 전 동작하는 메서드
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository); //같은 MemoryMemberRepository를 사용하기 위해 MemberService 객체에 넣어줌
    }

    @AfterEach //테스트 메서드들이 각각 실행이 끝날 때마다 동작하는 메서드
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() { //테스트 코드의 경우 메서드명을 직관적이게 한글로도 적음(테스트 코드는 빌드될 때 실제 코드에 포함되지 않기 때문에 상관없음)
        //given-when-then 패턴: 테스트 할 때 이렇게 세부분으로 나눠서 작성하면 도움이 됨
        //given - 이러한 상황이 주어져서 (기반으로 하는 데이터)
        Member member = new Member();
        member.setName("hello");

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

/*      //이렇게 try-catch로 해도 되지만 복잡해보이고 애매모호함. 그래서 아래 assertThrows() 문법을 제공
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/
        //왼쪽: 발생해야 하는(기대하는) 예외 클래스, 오른쪽: 해당 예외가 발생할 로직을 람다식으로 표현
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}