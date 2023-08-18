package sixman.stackoverflow.domain.answerrecommend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.answerrecommendexception.RecommendationChangeNotAllowedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
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

//    public void recommendAnswer(Long answerId, TypeEnum type) {
//        Long memberId = SecurityUtil.getCurrentId();
//        Answer answer = answerRepository.findById(answerId)
//                .orElseThrow(() -> new AnswerNotFoundException());
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException());
//
//
//        Optional<AnswerRecommend> existingRecommend = answerRecommendRepository.findByAnswerAndMember(answer, member);
//
//        if (existingRecommend.isPresent()) {
//            AnswerRecommend recommend = existingRecommend.get();
//            if (recommend.getType() == type) {
//
//                answerRecommendRepository.delete(recommend);
//            } else {
//
//                recommend.setType(type);
//                answerRecommendRepository.save(recommend);
//            }
//        } else {
//
//            AnswerRecommend newRecommend = AnswerRecommend.builder()
//                    .type(type)
//                    .member(member)
//                    .answer(answer)
//                    .build();
//            answerRecommendRepository.save(newRecommend);
//        }
//
//        if (type == TypeEnum.UPVOTE) {
//            answer.increaseRecommendCount();
//        } else if (type == TypeEnum.DOWNVOTE) {
//            answer.increaseDownvoteCount();
//        }
//        answerRepository.save(answer);
//    }
//}

    public void recommendAnswer(Long answerId, TypeEnum recommendationType) {
        Long memberId = SecurityUtil.getCurrentId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        AnswerRecommend existingRecommendation = answerRecommendRepository.findByAnswerAndMember(answer, member);

        int currentRecommendCount = answer.getRecommend();

        if (existingRecommendation != null) {
            if (existingRecommendation.getType() == recommendationType) {
                currentRecommendCount -= (recommendationType == TypeEnum.UPVOTE) ? 1 : -1;
                answerRecommendRepository.delete(existingRecommendation);
            } else {
                if (recommendationType == TypeEnum.DOWNVOTE) {
                    // 이미 추천한 상태에서 비추천을 요청할 경우 추천을 취소하고 비추천 증가
                    currentRecommendCount -= 1;
                    answerRecommendRepository.delete(existingRecommendation);
                    existingRecommendation.setType(recommendationType);
                    answerRecommendRepository.save(existingRecommendation);
                } else if (recommendationType == TypeEnum.UPVOTE) {
                    // 이미 비추천한 상태에서 추천을 요청할 경우 비추천을 취소하고 추천 증가
                    currentRecommendCount += 1;
                    answerRecommendRepository.delete(existingRecommendation);
                }
            }
        } else {
            currentRecommendCount += (recommendationType == TypeEnum.UPVOTE) ? 1 : -1;
            AnswerRecommend answerRecommend = AnswerRecommend.builder()
                    .type(recommendationType)
                    .member(member)
                    .answer(answer)
                    .build();

            answerRecommendRepository.save(answerRecommend);
        }

        answer.setRecommend(currentRecommendCount);
        answerRepository.save(answer);
    }
}






