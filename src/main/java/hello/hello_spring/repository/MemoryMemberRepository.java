package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    //static을 붙임으로써 MemoryMemberRepository 객체마다 store를 각각 따로 만드는게 아닌 클래스의 변수(static 변수)로 만들어 아무리 MemoryMemberRepository 객체가 여러 개여도 모두 하나의 store를 공유하도록 함
    //혹시나 MemoryMemberRepository 인스턴스가 여러 개 생겨도 같은 회원 데이터를 바라보도록 static을 사용한 것
    private static Map<Long, Member> store = new HashMap<>(); //회원 데이터를 임시로 저장하기 위해 Map 사용
    private static long sequence = 0L; //키의 값을 생성해주는 변수

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    //Optional<Member>의 의미
    //Member가 있을 수도, 없을 수도 있음을 표현
    //값이 있으면 Optional 안에 Member가 들어있고, 없으면 비어있음
    //값이 없을 수 있으므로 null 대신 Optional로 감싸서 값의 존재 여부를 명확하게 표현
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //Null이 반환될 가능성이 있는 경우 Optional.ofNullable()로 감쌈. 이렇게 하면 클라이언트가 무엇을 해도 영향을 안 받음.
    }

    @Override
    //member -> member.getName().equals(name) : 말로 풀면 "Member 하나를 받아서 → 이름이 같은지 검사해줘."
    public Optional<Member> findByName(String name) {
        return store.values().stream() //store.values() : Map에 들어있는 Member들만 꺼내는 것, .stream() : 루프를 돌려 Member들을 하나씩 다 검사함
                .filter(member -> member.getName().equals(name)) //그 중 Member 하나를 가져와서 그 Member의 name이 매개변수로 들어온 name과 같은지 검사함(.filter로 필터링한다는 그런 의미로 보면 됨)
                .findAny(); //조건에 맞는 Member를 하나 찾으면 반환(Optional로 감싸서 반환)
    }

    @Override
    public List<Member> findAll() { //Member들이 담긴 구조는 Map인데 여긴 List로 반환하도록 되어있음
        return new ArrayList<>(store.values()); //ArrayList<>를 만들어 Map에 들어있는 Member들을 List에 담고 반환시킴
    }
}
