package next.api;

import core.bean.annotations.Controller;
import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import lombok.extern.slf4j.Slf4j;
import next.model.Question;
import next.model.User;
import next.service.QuestionService;
import next.util.ApiResult;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/qna")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @Inject
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    public QuestionService getQnaService() {
        return questionService;
    }

    @RequestMapping(value = "/list", method = HttpMethod.GET)
    public ResponseEntity<ApiResult.ApiSuccessResult<List<Question>>> list() {
        List<Question> questions = questionService.findAll();
        return ResponseEntity.ok().body(ApiResult.success(questions));
    }

    @RequestMapping(value = "/delete", method = HttpMethod.POST)
    public ResponseEntity<?> deleteQuestion(
        @RequestParam("questionId") Long questionId,
        HttpSession session
    ) {
        User loginedUser = UserSessionUtils.getUserFromSession(session);
        if (loginedUser == null) {
            return ResponseEntity.unauthorized().build();
        }
        try {
            questionService.delete(loginedUser, questionId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
        return ResponseEntity.ok().body(ApiResult.success("질문이 삭제되었습니다."));
    }
}
