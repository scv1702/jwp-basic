package next.api;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
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

    private QuestionDao questionDao = new QuestionDao();
    private QuestionService questionService = new QuestionService(questionDao, new AnswerDao());

    public QuestionController() {
    }

    public QuestionController(QuestionDao questionDao, QuestionService questionService) {
        this.questionDao = questionDao;
        this.questionService = questionService;
    }

    @RequestMapping(value = "/list", method = HttpMethod.GET)
    public ResponseEntity<ApiResult.ApiSuccessResult<List<Question>>> list() {
        List<Question> questions = questionDao.findAll();
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
            questionService.deleteQuestion(loginedUser, questionId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
        return ResponseEntity.ok().body(ApiResult.success("질문이 삭제되었습니다."));
    }
}
