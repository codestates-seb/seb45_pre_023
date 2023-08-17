package sixman.stackoverflow.domain.answer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AnswerServiceTest extends ServiceTest {

    @Test
    @DisplayName("답변의 content, questionId 를 받아서 답변을 생성한다.")
    void createAnswer() {
    }

    @Test
    @DisplayName("답변 생성 시 존재하지 않는 questionId 이면 QuestionNotFoundException 이 발생한다.")
    void createAnswerException() {


    }

    @Test
    @DisplayName("answerId 를 통해 답변을 찾아서 반환한다.")
    void findAnswer() {
    }

    @Test
    @DisplayName("답변 조회 시 존재하지 않는 answerId 이면 AnswerNotFoundException 을 반환한다.")
    void findAnswerException() {
    }

    @Test
    @DisplayName("questionId 를 통해 답변 목록을 페이징으로 찾아서 반환한다.")
    void findAnswers() {
    }

    @Test
    @DisplayName("answerId, content 를 통해 답변을 수정한다.")
    void updateAnswer() {
    }

    @Test
    @DisplayName("답변 수정 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.")
    void updateAnswerException() {


    }

    @Test
    @DisplayName("답변 수정 시 다른 사람의 answer 를 수정하려고 하면 MemberAccessDeniedException 이 발생한다.")
    void updateAnswerMemberException() {


    }

    @Test
    @DisplayName("answerId 를 통해 답변을 삭제한다.")
    void deleteAnswer() {
    }

    @Test
    @DisplayName("답변 삭제 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.")
    void deleteAnswerException() {

    }

    @Test
    @DisplayName("답변 삭제 시 다른 사람의 answer 를 삭제하려고 하면 MemberAccessDeniedException 이 발생한다.")
    void deleteAnswerMemberException() {

    }
}