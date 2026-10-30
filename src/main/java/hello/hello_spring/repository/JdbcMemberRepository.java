package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource; //DB와 연동하려면 dataSource가 필요함

    public JdbcMemberRepository(DataSource dataSource) { //스프링이 우리가 'application.properties'에 입력한 DB 정보를 읽고 dataSource를 만들어서 주입해줌
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";

        Connection conn = null; //DB와 연결하는 객체
        PreparedStatement pstmt = null; //SQL문을 실행하는 객체
        ResultSet rs = null; //SQL 실행 결과를 담는 객체

        try {
            conn = getConnection(); //DB 연결을 가져와 conn에 저장
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //첫번째: sql, 두번째: SQL 실행 후 DB에서 자동 생성된 id를 반환받기 위한 옵션

            pstmt.setString(1, member.getName()); //sql 첫번째 인덱스('?')에 member.getName()을 넣겠다는 의미

            pstmt.executeUpdate(); //DB에 실제 쿼리가(insert문) 이때 전송됨. 데이터 저장은 executeUpdate()
            rs = pstmt.getGeneratedKeys(); //"Statement.RETURN_GENERATED_KEYS"랑 매칭됨에 따라 DB에 데이터가 삽입되면서 자동 생성된 id가 반환됨

            if (rs.next()) { //rs에 읽을 데이터가 있는지 확인
                member.setId(rs.getLong(1)); //값이 있으면 값(id)을 꺼내서 member의 id에 세팅함
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs); //중요! 사용이 끝난 DB 자원은 반드시 정리하고 반환(자원을 릴리즈한다: 사용하던 자원을 다 쓴 후 반납한다)
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id); //sql의 첫번째 인덱스('?')에 id를 세팅한다는 의미

            rs = pstmt.executeQuery(); //DB에 쿼리 전송됨. 데이터 조회는 executeQuery()

            if(rs.next()) { //rs에 다음 행이 있으면(즉, rs에 읽을 데이터가 있으면(값이 있으면 = DB에서 조회된 데이터가 있으면))
                Member member = new Member(); //Member 객체 생성
                member.setId(rs.getLong("id")); //rs에서 id 꺼내서 member의 id에 세팅
                member.setName(rs.getString("name")); //rs에서 name 꺼내서 member의 name에 세팅
                return Optional.of(member); //Optional로 감싸서 member 반환
            } else {
                return Optional.empty(); //rs에 값이 없으면(DB에서 조회되는 데이터가 없으면) 빈 Optional 객체 반환
            }

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>(); //member 전체 조회이기 때문에 List 컬렉션 생성
            while (rs.next()) { //rs에 값이 있으면 루프 돌리면서 Member 객체 생성 후 id와 name 세팅
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member); //List에 member들을 쭉 담음
            }
            return members;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource); //dataSource를 이용해 DB 연결 (연결할 때 스프링 프레임워크를 통해서 커넥션을 써야하므로 DataSourceUtils를 통해 getConnection() 해야함)
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource); //닫을 때도 DataSourceUtils를 통해 release 해줘야함
    }
}
