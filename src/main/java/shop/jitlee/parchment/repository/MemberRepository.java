package shop.jitlee.parchment.repository;
import org.springframework.data.jpa.repository.Query;
import shop.jitlee.parchment.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    @Query("select m.id from Member m where m.username = :username")
    Long findByUsernameGetId(String username);
}
