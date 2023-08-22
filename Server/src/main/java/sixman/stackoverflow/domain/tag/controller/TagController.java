package sixman.stackoverflow.domain.tag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.tag.service.TagService;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<ApiSingleResponse<List<QuestionTagResponse>>> getTags() {

        List<QuestionTagResponse> tags = tagService.getTags();

        return ResponseEntity.ok(ApiSingleResponse.ok(tags));
    }
}
