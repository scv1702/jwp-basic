package next.api;

import core.web.RequestMethod;
import core.web.ResponseEntity;
import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.ResponseBody;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.model.Answer;
import next.util.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping("/api/qna")
public class DeleteAnswerController {

    private static final Logger log = LoggerFactory.getLogger(DeleteAnswerController.class);
    private AnswerDao answerDao = new AnswerDao();

    @RequestMapping(value = "/deleteAnswer", method = RequestMethod.POST)
    @ResponseBody
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
