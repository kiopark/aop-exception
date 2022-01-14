package first.study.exceptiontest.controller;

import first.study.exceptiontest.domain.Member;
import first.study.exceptiontest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping
  public long saveMember(@RequestBody @Valid Member member) {
    return memberService.signUp(member);
  }

  @GetMapping("/{id}") // /members/0
  public Member searchMember(@PathVariable Long id) {
    return memberService.searchMember(id);
  }

  @GetMapping("/param")
  public List<Member> searchMembrList(@RequestParam String name) {
    return memberService.searchByName(name);
  }
}
