package first.study.exceptiontest.service;

import first.study.exceptiontest.domain.Member;
import first.study.exceptiontest.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  // 객체(Bean) 주입 방법
  // 1. 수정자 주입
  // 2. 필드 주입
  // 3. 생성자 주입 방법 v
  private final MemberRepository memberRepository;

  public long signUp(Member member) {
    validateDuplicateMember(member);
    memberRepository.save(member);

    return member.getId();
  }

  // AOP 를 사용한 Exception 처리가 아님
  private void validateDuplicateMember(Member member) {
    List<Member> members = memberRepository.findByName(member.getName());

    if (!members.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다!");
    }
  }

  public Member searchMember(Long id) {
    return memberRepository.findById(id).get();
  }

  public List<Member> searchByName(String name) {
    return memberRepository.findByName(name);
  }
}
