package sixman.stackoverflow.domain.question.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sixman.stackoverflow.global.testhelper.ServiceTest;

public class QuestionServiceTest extends ServiceTest {

    @Test
    @DisplayName("질문 목록을 페이징 해서 10개의 리스트를 최신 순으로 조회한다.")
    public void getLatestQuestions(){

    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 조회한다.")
    public void getQuestionById(){

    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글의 tag 리스트를 조회한다.")
    public void getQuestionTags(){

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
