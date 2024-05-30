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

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/api/qna")
public class AddAnswerController {

    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);
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
        return ResponseEntity.ok().body(ApiResult.success("답변이 등록되었습니다.", answer));
    }
}
