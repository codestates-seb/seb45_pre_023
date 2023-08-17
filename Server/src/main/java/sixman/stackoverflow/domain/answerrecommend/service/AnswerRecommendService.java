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
            if (existingRecommendation.getType() == recommendationType) { // 같은 값이 들어오면 취소
                currentRecommendCount -= (recommendationType == TypeEnum.UPVOTE) ? 1 : -1;
                answerRecommendRepository.delete(existingRecommendation);
            }
            //else {

//                currentRecommendCount += (recommendationType == TypeEnum.UPVOTE) ? 2 : -2; // 변경 로직인데 어렵다
//                existingRecommendation.setType(recommendationType);
//                answerRecommendRepository.save(existingRecommendation);

                if (existingRecommendation.getType() == TypeEnum.UPVOTE && recommendationType == TypeEnum.DOWNVOTE) {
                    //추천 눌렀는데 비추 누를 시
                    throw new RecommendationChangeNotAllowedException();
                } else if (existingRecommendation.getType() == TypeEnum.DOWNVOTE && recommendationType == TypeEnum.UPVOTE) {
                    //비추 눌렀는데 추천 누를 시
                    throw new RecommendationChangeNotAllowedException();
            }
        } else {
            currentRecommendCount += (recommendationType == TypeEnum.UPVOTE) ? 1 : -1; //처음 추천 OR 비추천
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






