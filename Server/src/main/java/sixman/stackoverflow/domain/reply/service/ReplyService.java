package sixman.stackoverflow.domain.reply.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

    public Long createreply(ReplyCreateApiRequest request, Long answerId) {
        Long memberId = SecurityUtil.getCurrentId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        Reply reply = postReply(request, member, answer);
        replyRepository.save(reply);

        return reply.getReplyId();
    }
//    public List<ReplyResponse> getRepiles(Long answerId) { // 리플목록
//        Answer answer= answerRepository.findById(answerId)
//                .orElseThrow(() -> new AnswerNotFoundException());
//
//        List<Reply> replies = replyRepository.findByAnswer(answer);
//
//        Long memberId = SecurityUtil.getCurrentId();
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException());
//
//
//        return replies.stream()
//                .map(reply -> createReplyResponse(reply, member)
//                .collect(Collectors.toList());
//    }



    @Transactional(readOnly = true)
    public Reply findreply(long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
    }

    public Reply updateReply(Long replyId, String newContent, Long memberId) {
        Reply replyUpdate = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());

        checkAccessAuthority(replyUpdate.getMember().getMemberId(), memberId);



        replyUpdate.setReplyContent(newContent);
        return replyRepository.save(replyUpdate);
    }


    public void deleteReply(long replyId, Long memberId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
        checkAccessAuthority(reply.getMember().getMemberId(), memberId);

        replyRepository.deleteById(replyId);
    }


    private Reply postReply(ReplyCreateApiRequest request, Member member, Answer answer) {
        return Reply.createReply(
                request.getContent(), member, answer
        );
    }

    private void checkAccessAuthority(Long replyAuthorId, Long memberId) {
        if (!replyAuthorId.equals(memberId)) {
            throw new MemberAccessDeniedException();
        }
    }
}
