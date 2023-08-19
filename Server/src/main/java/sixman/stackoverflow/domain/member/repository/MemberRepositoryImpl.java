package sixman.stackoverflow.domain.member.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.answerrecommend.entity.QAnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.dto.MemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.MemberQuestionData;
import sixman.stackoverflow.domain.member.repository.dto.QMemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.QMemberQuestionData;
import sixman.stackoverflow.domain.question.entity.QQuestion;
import sixman.stackoverflow.domain.questionrecommend.entity.QQuestionRecommend;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.entity.TypeEnum;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static sixman.stackoverflow.domain.answer.entitiy.QAnswer.answer;
import static sixman.stackoverflow.domain.answerrecommend.entity.QAnswerRecommend.answerRecommend;
import static sixman.stackoverflow.domain.member.entity.QMember.member;
import static sixman.stackoverflow.domain.question.entity.QQuestion.question;
import static sixman.stackoverflow.domain.questionrecommend.entity.QQuestionRecommend.*;
import static sixman.stackoverflow.domain.questiontag.entity.QQuestionTag.questionTag;
import static sixman.stackoverflow.domain.tag.entity.QTag.tag;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberQuestionData> findQuestionByMemberId(Long memberId, Pageable pageable) {

        Expression<Long> votes = JPAExpressions.select(questionRecommend.count())
                .from(questionRecommend)
                .where(questionRecommend.question.eq(question));

        Expression<Long> downs = JPAExpressions.select(questionRecommend.count())
                .from(questionRecommend)
                .where(questionRecommend.question.eq(question)
                        .and(questionRecommend.type.eq(TypeEnum.DOWNVOTE)));


        List<MemberQuestionData> memberQuestionDatas = queryFactory.select(new QMemberQuestionData(
                        question.questionId,
                        question.title,
                        question.views,
                        votes,
                        downs,
                        question.createdDate,
                        question.modifiedDate))
                .from(question)
                .where(question.member.memberId.eq(memberId))
                .orderBy(question.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int count = (int) queryFactory.selectFrom(QQuestion.question)
                .where(QQuestion.question.member.memberId.eq(memberId))
                .fetchCount();

        return new PageImpl<>(memberQuestionDatas, pageable, count);
    }

    @Override
    public Page<MemberAnswerData> findAnswerByMemberId(Long memberId, Pageable pageable) {

        Expression<Long> votes = JPAExpressions.select(answerRecommend.count())
                .from(answerRecommend)
                .where(answerRecommend.answer.eq(answer));

        Expression<Long> downs = JPAExpressions.select(answerRecommend.count())
                .from(answerRecommend)
                .where(answerRecommend.answer.eq(answer)
                        .and(answerRecommend.type.eq(TypeEnum.DOWNVOTE)));

        List<MemberAnswerData> memberAnswerDatas = queryFactory.select(new QMemberAnswerData(
                        answer.answerId,
                        answer.question.questionId,
                        answer.question.title,
                        answer.content,
                        votes,
                        downs,
                        answer.createdDate,
                        answer.modifiedDate))
                .from(answer)
                .where(answer.member.memberId.eq(memberId))
                .orderBy(answer.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(answer)
                .where(answer.member.memberId.eq(memberId))
                .fetchCount();

        return new PageImpl<>(memberAnswerDatas, pageable, count);
    }

    @Override
    public List<Tag> findTagByMemberId(Long memberId) {

        return queryFactory
                .select(tag)
                .distinct()
                .from(member)
                .innerJoin(member.questions, question)
                .innerJoin(question.questionTags, questionTag)
                .innerJoin(questionTag.tag, tag)
                .where(member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<Member> findByMemberIdWithInfo(Long memberId){
        return Optional.ofNullable(
                queryFactory
                .selectFrom(member)
                .join(member.myInfo).fetchJoin()
                .where(member.memberId.eq(memberId))
                .fetchOne());
    }
}
