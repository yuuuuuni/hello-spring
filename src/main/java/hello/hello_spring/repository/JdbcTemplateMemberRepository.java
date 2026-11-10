package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

    //JdbcTemplate은 이렇게 사용하면 된다. 스프링에서도 아래와 같은 스타일을 권장 (필드와 생성자 부분 확인!)
    private final JdbcTemplate jdbcTemplate; //JdbcTemplate이라는 객체가 따로 있다. 이걸 사용하면 된다.

    //@Autowired //생성자가 딱 하나만 있으면 해당 클래스가 스프링 빈으로 등록되어 있으면 @Autowired 생략 가능
    public JdbcTemplateMemberRepository(DataSource dataSource) { //jdbcTemplate은 주입이 안됨. 그래서 dataSource를 주입 받고 jdbcTemplate에 JdbcTemplate 객체를 new로 생성해서 dataSource를 넣어줘야함
        jdbcTemplate = new JdbcTemplate(dataSource); //스프링이 자동으로 dataSource를 인젝션 해줌(JdbcTemplate: DB 작업을 도와주는 도구)
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate); //스프링에서 제공하는 클래스(SimpleJdbcInsert: insert 작업을 간편하게 해주는 도구). jdbcTemplate이 넣어서 생성
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id"); //데이터를 저장할 테이블명은 member, DB에서 자동 생성하는 기본 키 컬럼은 id라고 설정

        Map<String, Object> parameters = new HashMap<>(); // DB에 저장할 데이터를 담을 Map 생성
        parameters.put("name", member.getName()); //Map의 키에는 DB 컬럼명인 "name"을, 값에는 저장할 member의 name 값을 넣음

        //new MapSqlParameterSource(parameters): parameters를 SQL 파라미터로 전달할 수 있도록 변환하는 객체
        //jdbcInsert.executeAndReturnKey(...): insert 실행 후 DB가 자동 생성한 id 값을 반환(그게 key에 들어감)
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)); //Number는 숫자를 표현하는 자바 클래스
        member.setId(key.longValue()); //key(id 값)를 long 타입으로 변환 후 Member 객체의 id에 저장
        return member; //id와 name이 모두 들어간 Member 객체 반환
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id); //jdbcTemplate.query()를 실행하면 조회된 데이터의 갯수와 상관없이 데이터가 List 형식으로 꺼내짐
        return result.stream().findAny(); //List에 들어 있는 데이터를 Stream으로 변환한 후, 그 요소 중 하나를 찾아 Optional로 감싸서 반환
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() { //RowMapper는 DB에서 조회한 한 행(row)을 자바의 Member 객체로 바꿔줌
        //아래의 코드를 람다 형식으로 바꾼 코드
        return (rs, rowNum) -> { //rs는 DB 조회 결과를 담고 있는 객체, rowNum은 조회 결과에서 현재 행이 몇 번째인지 나타내는 숫자
            Member member = new Member(); //새 Member 객체 생성
            member.setId(rs.getLong("id")); //DB에서 조회된 데이터의 현재 행의 id 값을 꺼내서 member의 id에 세팅
            member.setName(rs.getString("name")); //DB에서 조회된 데이터의 현재 행의 name 값을 꺼내서 member의 name에 세팅
            return member; //완성된 Member 객체 반환
        };

        /* return new RowMapper<Member>() { //익명 클래스라는 방식으로 RowMapper를 구현
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        } */
    }
}
