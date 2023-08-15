package sixman.stackoverflow.domain.answerrecommend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;

import java.util.Optional;

@Service
@Transactional
public class AnswerRecommendService {

    private final AnswerRecommendRepository answerRecommendRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;


    public AnswerRecommendService(AnswerRecommendRepository answerRecommendRepository,
                                  AnswerRepository answerRepository,
                                  MemberRepository memberRepository) {
        this.answerRecommendRepository = answerRecommendRepository;
        this.answerRepository = answerRepository;
        this.memberRepository = memberRepository;
    }

    public void recommendAnswer(Long answerId, Long memberId, TypeEnum type) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());


        Optional<AnswerRecommend> existingRecommend = answerRecommendRepository.findByAnswerAndMember(answer, member);

        if (existingRecommend.isPresent()) {
            AnswerRecommend recommend = existingRecommend.get();
            if (recommend.getType() == type) {

                answerRecommendRepository.delete(recommend);
            } else {

                recommend.setType(type);
                answerRecommendRepository.save(recommend);
            }
        } else {

            AnswerRecommend newRecommend = AnswerRecommend.builder()
                    .type(type)
                    .member(member)
                    .answer(answer)
                    .build();
            answerRecommendRepository.save(newRecommend);
        }

        if (type == TypeEnum.UPVOTE) {
            answer.increaseRecommendCount();
        } else if (type == TypeEnum.DOWNVOTE) {
            answer.increaseDownvoteCount();
        }
        answerRepository.save(answer);
    }
}

