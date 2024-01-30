package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);   //트랜잭션 시작!!
            //비지니스 로직
            businessLogic(con, fromId, toId, money);
            con.commit();   //성공시 커밋!!

        } catch (Exception e) {
            con.rollback(); //실패시 롤백!!
            throw new IllegalStateException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception e) {
                    log.info("error", e);
                }
            }
        }
    }
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생"); }

    }

    private void businessLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }
}
/**
 * 트랜잭션은 비지니스 로직이 있는 서비스 계층에서 시작하는 것이 좋다.
 * but, 트랜잭션을 사용하기 위해 JDBC 기술에 의존해야 한다는 문제가 있다.
 * JPA로 바꿔서 사용하게 되면 이 코드는 모두 수정해야 한다. sol) 트랜잭션 추상화
 * 핵심 비지니스 로직과 JDBC 기술이 섞여 있다.
 */
