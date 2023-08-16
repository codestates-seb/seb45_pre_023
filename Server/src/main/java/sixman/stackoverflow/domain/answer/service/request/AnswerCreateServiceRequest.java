package sixman.stackoverflow.domain.answer.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class AnswerCreateServiceRequest {

    private String content;
}
