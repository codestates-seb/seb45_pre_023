package sixman.stackoverflow.domain.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;

import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;


import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    public Long createAnswer(AnswerCreateApiRequest request) {


        Answer answer = postAnswer(request);

        return answerRepository.save(answer).getAnswerId();

    }
    @Transactional(readOnly = true)
    public Answer findAnswer(long replyId) {
        return answerRepository.findById(replyId)
                .orElseThrow(() -> new AnswerNotFoundException());
    }

    public Answer updateAnswer(Long answerId, String newContent) {
        Answer answerUpdate = answerRepository.findById(answerId)
                .orElseThrow(() -> new AnswerNotFoundException());


       answerUpdate.setAnswerContent(newContent);
        return answerRepository.save(answerUpdate);
    }

    public void deleteAnswer(long answerId) {
        answerRepository.deleteById(answerId);
    }




    private Answer postAnswer(AnswerCreateApiRequest request) {
        return Answer.createAnswer(
                request.getContent()
        );
    }
}

