package next.api;

import core.context.annotations.Controller;
import core.context.annotations.Inject;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import core.web.annotations.ResponseBody;
import next.model.Answer;
import next.model.User;
import next.service.AnswerService;
import next.util.ApiResult;
import next.util.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/api/qna")
public class AnswerController {

    private static final Logger log = LoggerFactory.getLogger(AnswerController.class);

    private final AnswerService answerService;

    @Inject
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @RequestMapping(value = "/addAnswer", method = HttpMethod.POST)
    public ResponseEntity<?> addAnswer(
        @RequestParam("contents") String contents,
        @RequestParam("questionId") Long questionId,
        HttpSession session
    ) {
        if (!UserSessionUtils.isLogined(session)) {
            return ResponseEntity.unauthorized().body(ApiResult.error("로그인 후 이용해주세요."));
        }
        User user = UserSessionUtils.getUserFromSession(session);
        Answer inserted = answerService.insert(user, questionId, contents);
        return ResponseEntity.ok().body(ApiResult.success("답변이 등록되었습니다.", inserted));
    }

    @RequestMapping(value = "/deleteAnswer", method = HttpMethod.POST)
    public ResponseEntity<?> deleteAnswer(@RequestParam("answerId") Long answerId, HttpSession session) {
        if (!UserSessionUtils.isLogined(session)) {
            return ResponseEntity.unauthorized().body(ApiResult.error("로그인 후 이용해주세요."));
        }
        User user = UserSessionUtils.getUserFromSession(session);
        try {
            answerService.delete(user, answerId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ApiResult.error(e.getMessage()));
        }
        return ResponseEntity.ok().body(ApiResult.success("답변이 삭제되었습니다."));
    }
}
