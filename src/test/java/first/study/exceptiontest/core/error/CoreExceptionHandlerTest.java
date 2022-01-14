package first.study.exceptiontest.core.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import first.study.exceptiontest.common.error.exception.ErrorCode;
import first.study.exceptiontest.domain.Member;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
// @WebMvcTest : @Controller 테스트용, Controller 요청 응답을 테스트, 시큐리티와 필터까지 테스트 가능, @Service @Repository 객체는 메모리에 안올림
@AutoConfigureMockMvc // 컨트롤러 뿐만아니라 @Service @Respository가 붙은 객체들도 모두 메모리에 올림, 보다 세밀한 제어를 위해 사용하며 전체 애플리케이션 구성을 로드하여 사용하는 경우
@Rollback(value = false)
class CoreExceptionHandlerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @PersistenceContext // Persistence : 영속성
  EntityManager em;

  @BeforeEach
  public void 회원_등록() {
    // given
    Member member1 = new Member("111", "kio@gmail.com");
    Member member2 = new Member("222", "kiho@gmail.com");

    em.persist(member1); // 영속성 컨테스트에 저장 ( 일종의 proxy 객체, 실제 디비에 저장하지 않음 )
    em.persist(member2);
    em.flush();
  }

  @Test
  public void 회원_등록_null_포함일때() throws Exception {
    // given
    Member member = Member.builder()
        .name("kio")
        .build();

    // when
    ResultActions resultActions = requestPostMember(member);

    // then
    resultActions
        .andExpect(status().isBadRequest());

  }

  @Test
  public void 지원하지않는_Http_method_요청() throws Exception {
    // when
    ResultActions resultActions = requestGetMember(1L);

    // then
    resultActions
        .andExpect(status().isMethodNotAllowed())
        .andExpect(jsonPath("message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
  }

  @Test
  public void 회원_등록_name_size_불일치() throws Exception {
    //given
    Member member = Member.builder()
        .name("kiho")
        .email("kihozzang")
        .build();

    // when
    ResultActions resultActions = requestPostMemberName(member);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()));
  }

  @Test
  public void 회원_등록_type_mismatch() throws Exception {
    //when
    ResultActions resultActions = requestGetMemberList("test");

    //then
    resultActions
        .andExpect(status().isMethodNotAllowed());
  }

  private ResultActions requestPostMemberName(Member member) throws Exception {
    return mvc.perform(post("/members")
        .content(objectMapper.writeValueAsString(member))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private ResultActions requestGetMemberList(String name) throws Exception {
    return mvc.perform(get("/members/{id}", name)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private ResultActions requestGetMember(long memberId) throws Exception {
    return mvc.perform(post("/members/{id}", memberId)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  private ResultActions requestPostMember(Member member) throws Exception {
    return mvc.perform(post("/members")
        .content(objectMapper.writeValueAsString(member))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

}