package sixman.stackoverflow.domain.answer.service;

import lombok.Getter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;

import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.AnswerSortRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final ReplyRepository replyRepository;

    private final AnswerRecommendRepository answerRecommendRepository;

    public AnswerService(AnswerRepository answerRepository,
                         MemberRepository memberRepository,
                         QuestionRepository questionRepository,
                         ReplyRepository replyRepository,
                         AnswerRecommendRepository answerRecommendRepository) {
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
        this.replyRepository = replyRepository;
        this.answerRecommendRepository = answerRecommendRepository;
    }


    public Long createAnswer(AnswerCreateServiceRequest request, Long questionId) {

        Long memberId = SecurityUtil.getCurrentId();
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        Question question = questionOptional.orElseThrow(QuestionNotFoundException::new);

        Answer answer = postAnswer(request, member, question);

        answerRepository.save(answer);
        return answer.getAnswerId();

    }

    public AnswerResponse findAnswer(Long answerId) {
        if (answerId == null) {
            throw new AnswerNotFoundException();
        }
        Long memberId = SecurityUtil.getCurrentId();

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answer = answerOptional.orElseThrow(AnswerNotFoundException::new);

        TypeEnum answerRecommendType = getRecommendTypeForMemberAndAnswer(member, answer);

        return AnswerResponse.createAnswerResponse(answer, answerRecommendType);


    }


    public Page<AnswerResponse> findAnswers(Long questionId, Pageable pageable) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();

            Page<Answer> answers = answerRepository.findAllByQuestion(question, pageable);
            Pageable replyPageable = PageRequest.of(0,5, Sort.by(AnswerSortRequest.CREATED_DATE.getValue()).descending());

            Page<AnswerResponse> answerResponses = answers.map(answer -> {
                Page<Reply> replyPage = replyRepository.findByAnswer(answer, replyPageable);
                return AnswerResponse.of(answer, replyPage);
            });

            return answerResponses;
        }else{
            throw new QuestionNotFoundException();
        }
    }

    public Answer updateAnswer(Long answerId, String newContent) {
        Long memberId = SecurityUtil.getCurrentId();
        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answerUpdate = answerOptional.orElseThrow(AnswerNotFoundException::new);


        checkAccessAuthority(answerUpdate.getMember().getMemberId(), memberId);


        answerUpdate.setAnswerContent(newContent);
        return answerRepository.save(answerUpdate);
    }

    public void deleteAnswer(long answerId) {
        Long memberId = SecurityUtil.getCurrentId();
        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answer = answerOptional.orElseThrow(AnswerNotFoundException::new);

        checkAccessAuthority(answer.getMember().getMemberId(), memberId);

        try {
            answerRepository.deleteById(answerId);
        } catch (EmptyResultDataAccessException ex) {
            throw new AnswerNotFoundException();
        }
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

    private TypeEnum getRecommendTypeForMemberAndAnswer(Member member, Answer answer) {
        Optional<AnswerRecommend> answerRecommendOptional = answerRecommendRepository.findByMemberAndAnswer(member, answer);
        return answerRecommendOptional.map(AnswerRecommend::getType).orElse(null);
    }

}

