package sixman.stackoverflow.domain.reply.service;

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
        Long memberId = SecurityUtil.getCurrentId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());

        Reply reply = postReply(request, member, answer);
        replyRepository.save(reply);

        return reply.getReplyId();
    }
//    public List<ReplyResponse> getReplies(Long answerId) {
//
//        Long memberId = SecurityUtil.getCurrentId();
//
//        Answer answer= answerRepository.findById(answerId)
//                .orElseThrow(() -> new AnswerNotFoundException());
//
//        List<Reply> replies = replyRepository.findRepliesByAnswer(answer);
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new MemberNotFoundException());
//
//        // 리스트로 된 replies를 맵에 넣어서
//        // 하나 하나 꺼내서 멤버 아이디닉네임값과 가져와서 그걸 ReplyResponse저장한 후 반환
//
//        return replies.stream()
//
//                .map(reply -> createReplyResponse(reply, member))
//                .collect(Collectors.toList());
//    }

    public Page<ReplyResponse> getRepliesPaged(Long answerId, Pageable pageable) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());


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
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
        return ReplyResponse.createReplyResponse(reply);
    }

    public Reply updateReply(Long replyId, String newContent) {

        Long memberId = SecurityUtil.getCurrentId();

        Reply replyUpdate = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());

        checkAccessAuthority(replyUpdate.getMember().getMemberId(), memberId);



        replyUpdate.setReplyContent(newContent);
        return replyRepository.save(replyUpdate);
    }


    public void deleteReply(long replyId) {
        Long memberId = SecurityUtil.getCurrentId();
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
        checkAccessAuthority(reply.getMember().getMemberId(), memberId);

        replyRepository.deleteById(replyId);
    }


    private Reply postReply(ReplyCreateServiceRequest request, Member member, Answer answer) {
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
