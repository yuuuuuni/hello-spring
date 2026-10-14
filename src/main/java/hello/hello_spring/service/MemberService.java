package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

//회원 repository와 domain을 활용해서 실제 비즈니스 로직을 작성하는 클래스
public class MemberService {
    //회원 서비스단을 만드려면 MemoryMemberRepository 필요(MemoryMemberRepository 클래스를 new로 객체 생성해야 사용할 수 있으니까)
    private final MemberRepository memberRepository;

    //생성자 추가
    //이렇게 MemberService 입장에서 직접 MemoryMemberRepository를 new 하지 않고 외부에서 MemoryMemberRepository를 넣어주는 것을 의존성 주입(DI)라고 함
    public MemberService(MemberRepository memberRepository) { //외부에서 MemberRepository를 받아서 넣어주도록 수정
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        /* ifPresent() 뜻: result의 값이 null이 아니라 어떠한 값이 있으면 괄호 안의 로직이 동작한다는 의미. Optional이기 때문에 가능함.
       즉, result가 그냥 객체가 반환된게 아니라 Optional로 감싸서 반환된 객체이기 때문에 Optional 안에 Member 객체가 있는 것임.
       이렇게 Optional을 통해서 아래와 같이 여러 종류의 메서드를 사용할 수 있음.
       과거에는 그냥 null을 반환후 if (result != null) {...} 이런식으로 작성했다면,
       요즘에는 null일 가능성이 있으면 Optional로 한번 감싸서 반환하고 감싼 덕분에 ifPresent 같은 메서드를 사용할 수 있음. */

        //Tip. 'Optional<Member> result' 이런식으로 Optional을 바로 반환 받는게 좋지 않음(일단 복잡해보이고 안예쁨)
        // Optional<Member> result = memberRepository.findByName(member.getName());
        // result.ifPresent(m -> {
        memberRepository.findByName(member.getName()) //반환값을 담는 result를 없애고 이 코드 실행문 자체를 반환값(Optional<Member>)이라고 이해하면 됨
                .ifPresent(m -> { //반환된 객체(Optional<Member>)를 m이라는 변수로 전달(이때 m은 member의 약자로 임의로 정함)
                    throw new IllegalStateException("이미 존재하는 회원입니다."); //현재 상태가 부적절한 상태일 때 발생하는 예외 (throw: 예외를 발생시켜라, new IllegalStateException(): 발생시킬 예외 객체를 만들어라, 메시지명: 예외가 발생될 때 뜨는 메시지)
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 개인 회원 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
