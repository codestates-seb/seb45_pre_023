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



    public void recommendAnswer(Long answerId, TypeEnum recommendationType) {
        Long memberId = SecurityUtil.getCurrentId();

        if (memberId == null) {
            throw new MemberNotFoundException();
        }

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answer = answerOptional.orElseThrow(AnswerNotFoundException::new);

        AnswerRecommend existingRecommendation = answerRecommendRepository.findByAnswerAndMember(answer, member);

        Integer currentRecommendCount = answer.getRecommend();

        if (currentRecommendCount == null) {
            currentRecommendCount = 0; // 0으로 초기화 안해줘서 그런 듯
        }

        if (existingRecommendation != null) {
            if (existingRecommendation.getType() == recommendationType) {
                // 이미 추천 또는 비추천 상태를 변경할 경우 삭제 후 저장하지 않고 추천 수만 변경
                if (recommendationType == TypeEnum.UPVOTE) {
                    currentRecommendCount -= 1;
                } else if (recommendationType == TypeEnum.DOWNVOTE) {
                    currentRecommendCount += 1;
                }
                answerRecommendRepository.delete(existingRecommendation);
            } else { // existingRecommendation.getType() =! recommendationType
                // 이미 추천한 상태에서 비추천을 요청할 경우 추천을 취소하고 비추천 증가
                if (recommendationType == TypeEnum.DOWNVOTE) {
                    currentRecommendCount -= 2;
                    answerRecommendRepository.delete(existingRecommendation);
                    AnswerRecommend answerRecommend = AnswerRecommend.builder()
                            .type(recommendationType)
                            .member(member)
                            .answer(answer)
                            .build();

                    answerRecommendRepository.save(answerRecommend);
                }
                // 이미 비추천한 상태에서 추천을 요청할 경우 비추천을 취소하고 추천 증가
                else if (recommendationType == TypeEnum.UPVOTE) {
                    currentRecommendCount +=2;
                    answerRecommendRepository.delete(existingRecommendation);
                    AnswerRecommend answerRecommend = AnswerRecommend.builder()
                            .type(recommendationType)
                            .member(member)
                            .answer(answer)
                            .build();

                    answerRecommendRepository.save(answerRecommend);
                }
            }
        } else {
            // 추천 또는 비추천 상태가 없을 경우 새로운 추천을 생성
            if (recommendationType == TypeEnum.UPVOTE) {
                currentRecommendCount += 1;
            } else if (recommendationType == TypeEnum.DOWNVOTE) {
                currentRecommendCount -= 1;
            }
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












