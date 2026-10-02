package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*; //assertThat(..) 기능을 편하게 사용하기 위해 static import 함.(원래는 assertThat(..) 앞에 Assertions. 붙여야함)

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    //테스트가 끝날 때마다 리포지토리를 깔끔하게 지워주는 코드 추가
    @AfterEach //아래의 테스트 메서드들이 실행이 끝날 때마다 이 메서드가 동작함(일종의 콜백 메서드)
    public void afterEach() {
        repository.clearStore(); //테스트 메서드들이 각각 실행이 끝날 때마다 afterEach()가 동작하면서 store의 데이터들을 모두 지움
    }

    @Test //Test 어노테이션을 달면 이 메서드를 실행할 수 있음
    public void save() { //이 메서드명은 MemoryMemberRepository 클래스의 save() 메서드를 검증하기 위해 작성하는 메서드이므로, 메서드명을 이해하기 쉽도록 동일하게 save()라고 지은 것임
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();//Optional에서 값을 꺼낼 때는 .get()으로 꺼낼 수 있음
        //System.out.println("result = " + (result == member)); //둘이 같은건지 단순 비교해서 출력
        //Assertions.assertEquals(member, result); //둘이 같은건지 비교해주는 기능(왼쪽이 기대값, 오른쪽이 실제값) (참이면 테스트가 성공, 거짓이면 테스트가 실패)
        assertThat(result).isEqualTo(member); //둘이 같은건지 비교해주는 기능, 요즘은 이 기능을 더 많이 사용(왼쪽이 실제값, 오른쪽이 기대값)
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
