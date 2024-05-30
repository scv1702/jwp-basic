package next.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.util.json.JsonConverter;
import core.web.RequestMethod;
import core.web.ResponseEntity;
import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
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

@Controller
@RequestMapping("/api/qna")
public class AddAnswerController {

    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);
    private QuestionDao questionDao = new QuestionDao();
    private AnswerDao answerDao = new AnswerDao();
    private JsonConverter jsonConverter = new JsonConverter();

    @RequestMapping(value = "/addAnswer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addAnswer(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return ResponseEntity.unauthorized().body(ApiResult.error("로그인 후 이용해주세요."));
        }
        Answer answer = new Answer(
            UserSessionUtils.getUserFromSession(req.getSession()),
            questionDao.findByQuestionId(Long.valueOf(req.getParameter("questionId"))),
            req.getParameter("contents")
        );
        answerDao.insert(answer);
        return ResponseEntity.created().body(ApiResult.success("답변이 등록되었습니다.", answer));
    }
}
