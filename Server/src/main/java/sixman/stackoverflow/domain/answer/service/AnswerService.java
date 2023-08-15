package sixman.stackoverflow.domain.answer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;

import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;


import java.util.List;

@Service
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository,
                         MemberRepository memberRepository,
                         QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
    }


    public Long createAnswer(AnswerCreateServiceRequest request, Long questionId) {

        Long memberId = SecurityUtil.getCurrentId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException());

        Answer answer = postAnswer(request, member, question);

        answerRepository.save(answer);
        return answer.getAnswerId();

    }
    @Transactional(readOnly = true)
    public AnswerResponse findAnswer(long answerId) {
//        return answerRepository.findById(replyId)
//                .orElseThrow(() -> new AnswerNotFoundException());

        return null;
    }

    //todo : 구현 필요 (answer 를 5개씩 페이징해서 조회)
    public Page<AnswerResponse> findAnswers(Long questionId, Pageable pageable) {
        return null;
    }

    public Answer updateAnswer(Long answerId, String newContent, Long memberId) {

        Answer answerUpdate = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        checkAccessAuthority(answerUpdate.getMember().getMemberId(), memberId);


        answerUpdate.setAnswerContent(newContent);
        return answerRepository.save(answerUpdate);
    }

    public void deleteAnswer(long answerId, Long memberId) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        checkAccessAuthority(answer.getMember().getMemberId(), memberId);
        answerRepository.deleteById(answerId);
    }





    private Answer postAnswer(AnswerCreateServiceRequest request, Member member, Question question) {
        return Answer.createAnswers(
                request.getContent(), member, question
        );
    }

    private void checkAccessAuthority(Long answerAuthorId, Long memberId) {
        if (!answerAuthorId.equals(memberId)) {
            throw new MemberAccessDeniedException();
        }
    }
}
