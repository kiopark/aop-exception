package first.study.exceptiontest.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// ORM : Object Relation Mapping (객체와 테이블을 각각 분리해서 사용하는데, 객체에서 테이블에 대한 설정을 함)
@Entity
// setter 지양
@Getter
// default table name : Member == Entity명
@Table(name = "member")
// 양방향 연관관계를 끊어내기 위함
@ToString(of = {"name", "email"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Base {

  @Id @GeneratedValue
  @Column(name = "memberId", updatable = false)
  private Long id;

  @NotBlank
  @Size(max = 3)
  private String name;

  @NotBlank
  private String email;

  @Builder
  public Member(String name, String email) {
    this.name = name;
    this.email = email;
  }

}
