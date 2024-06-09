package next.api;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import core.web.annotations.ResponseBody;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.util.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/api/qna")
public class AnswerController {

    private static final Logger log = LoggerFactory.getLogger(AnswerController.class);
    private QuestionDao questionDao = new QuestionDao();
    private AnswerDao answerDao = new AnswerDao();

    @RequestMapping(value = "/addAnswer", method = HttpMethod.POST)
    public ResponseEntity<?> addAnswer(
        @RequestParam("contents") String contents,
        @RequestParam("questionId") Long questionId,
        HttpSession session
    ) {
        if (!UserSessionUtils.isLogined(session)) {
            return ResponseEntity.unauthorized().body(ApiResult.error("로그인 후 이용해주세요."));
        }
        Answer answer = new Answer(
            UserSessionUtils.getUserFromSession(session),
            questionDao.findByQuestionId(questionId),
            contents
        );
        answerDao.insert(answer);
        Answer inserted = answerDao.findByAnswerId(answer.getAnswerId())
            .orElseThrow(() -> new IllegalStateException("답변 등록에 실패했습니다."));
        return ResponseEntity.ok().body(ApiResult.success("답변이 등록되었습니다.", inserted));
    }

    @RequestMapping(value = "/deleteAnswer", method = HttpMethod.POST)
    public ResponseEntity<?> deleteAnswer(HttpServletRequest req, HttpServletResponse res) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return ResponseEntity.unauthorized().body(ApiResult.error("로그인 후 이용해주세요."));
        }
        log.info("parameter: {}", req.getParameterMap());
        Optional<Answer> answerOptional = answerDao.findByAnswerId(Long.valueOf(req.getParameter("answerId")));
        if (!answerOptional.isPresent()) {
            return ResponseEntity.notFound().body(ApiResult.error("존재하지 않는 답변입니다."));
        }
        Answer answer = answerOptional.get();
        if (!answer.getWriter().isSameUser(UserSessionUtils.getUserFromSession(req.getSession()))) {
            return ResponseEntity.forbidden().body(ApiResult.error("본인이 작성한 답변만 삭제할 수 있습니다."));
        }
        answerDao.delete(Long.valueOf(req.getParameter("answerId")));
        return ResponseEntity.ok().body(ApiResult.success("답변이 삭제되었습니다."));
    }
}
