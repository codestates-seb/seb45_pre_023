package sixman.stackoverflow.domain.reply.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse.createReplyResponse;


@Service
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    public ReplyService(ReplyRepository replyRepository, MemberRepository memberRepository, AnswerRepository answerRepository) {
        this.replyRepository = replyRepository;
        this.memberRepository = memberRepository;
        this.answerRepository = answerRepository;
    }

    public Long createReply(ReplyCreateServiceRequest request, Long answerId) {
        Long memberId = SecurityUtil.getCurrentId(); // 사실 인증만 하고, 멤버 id는 밑에 값에서 넣어야 더 깔끔할 것 같다.

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answer = answerOptional.orElseThrow(AnswerNotFoundException::new);

        Reply reply = postReply(request, answer, member);
        replyRepository.save(reply);

        return reply.getReplyId();
    }


    public Page<ReplyResponse> getRepliesPaged(Long answerId, Pageable pageable) {
        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        Answer answer = answerOptional.orElseThrow(AnswerNotFoundException::new);


        Page<Reply> repliesPage = replyRepository.findByAnswer(answer, pageable);

        Page<ReplyResponse> replyResponsesPage = repliesPage.map(reply -> ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .member(MemberInfo.of(reply.getMember()))
                .createdDate(reply.getCreatedDate())
                .updatedDate(reply.getModifiedDate())
                .build()
        );

        return replyResponsesPage;
    }




    @Transactional(readOnly = true)
    public ReplyResponse findReply(long replyId) {

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        Reply reply = replyOptional.orElseThrow(ReplyNotFoundException::new);

        return ReplyResponse.createReplyResponse(reply);
    }

    public Reply updateReply(Long replyId, String newContent) {

        Long memberId = SecurityUtil.getCurrentId();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        Reply replyUpdate = replyOptional.orElseThrow(ReplyNotFoundException::new);

        checkAccessAuthority(replyUpdate.getMember().getMemberId(), memberId);



        replyUpdate.setReplyContent(newContent);
        return replyRepository.save(replyUpdate);
    }


    public void deleteReply(long replyId) {
        Long memberId = SecurityUtil.getCurrentId();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        Reply reply = replyOptional.orElseThrow(ReplyNotFoundException::new);

        checkAccessAuthority(reply.getMember().getMemberId(), memberId);

        try {
            replyRepository.deleteById(replyId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ReplyNotFoundException();
        }
    }


    private Reply postReply(ReplyCreateServiceRequest request, Answer answer, Member member) {
        return Reply.createReply(
                request.getContent(), answer, member
        );
    }

    private void checkAccessAuthority(Long replyAuthorId, Long memberId) {
        if (!replyAuthorId.equals(memberId)) {
            throw new MemberAccessDeniedException();
        }
    }
}
