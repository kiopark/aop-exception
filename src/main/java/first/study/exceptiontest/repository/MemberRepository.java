package first.study.exceptiontest.repository;

import first.study.exceptiontest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  // select * from Member where name = :name;
  List<Member> findByName(String name);

}
