package sixman.stackoverflow.domain.question.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class QuestionServiceTest extends ServiceTest {


    @Test
    @DisplayName("질문 목록을 페이징 해서 10개의 리스트를 최신 순으로 조회한다.")
    public void getLatestQuestions() {

    }



    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 조회한다.")
    public void getQuestionById(){

    }


    @Test
    @DisplayName("생성된 question의 questionId를 반환한다.")
    public void createQuestion(){

    }

    @Test
    @DisplayName("questionId와 해당 질문글의 내용을 받아서 내용을 수정한다.")
    public void updateQuestion(){

    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 삭제한다.")
    public void deleteQuestion(){

    }

    @Test
    @DisplayName("questionId와 태그 리스트를 받아서 해당 질문글의 선택한 태그를 삭제한다.")
    public void removeTagsFromQuestion(){

    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 추천이나 비추천한다.")
    public void addQuestionRecommend(){

    }
}
