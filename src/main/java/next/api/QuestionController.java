package next.api;

import core.context.annotations.Controller;
import core.context.annotations.Inject;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.util.UserSessionUtils;
import next.model.Question;
import next.model.User;
import next.service.QuestionService;
import next.util.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/qna")
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

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
