package sixman.stackoverflow.domain.reply.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;

import java.util.List;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Long createreply(ReplyCreateApiRequest request) {

        Reply reply = postreply(request);

        return replyRepository.save(reply).getReplyId();
    }



    @Transactional(readOnly = true)
    public Reply findreply(long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());
    }

    public Reply updateReply(Long replyId, String newContent) {
        Reply replyUpdate = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFoundException());


        replyUpdate.setReplyContent(newContent);
        return replyRepository.save(replyUpdate);
    }


    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }




    private Reply postreply(ReplyCreateApiRequest request) { // 어렵다..
        return Reply.createReply(
                request.getContent()
        );
    }
}
