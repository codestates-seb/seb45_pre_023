package sixman.stackoverflow.domain.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;

import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;


import java.util.List;

@Service
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    // private final QuestionRepository questionRepository; 에러날까봐 주석처리

    public AnswerService(AnswerRepository answerRepository, MemberRepository memberRepository) {
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
    }


    public Long createAnswer(AnswerCreateApiRequest request, Long questionId) {

        Long memberId = SecurityUtil.getCurrentId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new QuestionNotFoundException()); 에러날까봐 주석처리

        //Answer answer = postAnswer(request, member);
        Answer answer = postAnswer(request, member);

        answerRepository.save(answer);
        return answer.getAnswerId();

    }
    @Transactional(readOnly = true)
    public Answer findAnswer(long replyId) {
        return answerRepository.findById(replyId)
                .orElseThrow(() -> new AnswerNotFoundException());
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




    private Answer postAnswer(AnswerCreateApiRequest request, Member member) {
        return Answer.createAnswer(
                request.getContent(),member
        );
    }
//    private Answer postAnswer(AnswerCreateApiRequest request, Member member, Question question) {
//        return Answer.createAnswer(
//                request.getContent(),member,question
//        );
//    }

    private void checkAccessAuthority(Long answerAuthorId, Long memberId) {
        if (!answerAuthorId.equals(memberId)) {
            throw new MemberAccessDeniedException();
        }
    }
}

