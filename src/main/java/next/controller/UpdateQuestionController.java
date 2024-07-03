package next.controller;

import core.bean.annotations.Controller;
import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.QuestionDao;
import next.model.Question;
import next.util.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
public class UpdateQuestionController {

    private static final Logger log = LoggerFactory.getLogger(UpdateQuestionController.class);

    private final QuestionDao questionDao;

    @Inject
    public UpdateQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @RequestMapping(value = "/update", method = HttpMethod.POST)
    public String createQuestion(
        @RequestParam("questionId") Long questionId,
        @RequestParam("title") String title,
        @RequestParam("contents") String contents,
        HttpSession session
    ) {
        Question question = questionDao.findByQuestionId(questionId);
        if (!UserSessionUtils.isSameUser(session, question.getWriter())) {
            return "redirect:/";
        }
        question.setTitle(title);
        question.setContents(contents);
        questionDao.update(question);
        return "redirect:/qna/show?questionId=" + questionId;
    }
}
