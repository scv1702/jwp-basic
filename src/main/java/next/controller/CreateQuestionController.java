package next.controller;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.web.Model;
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
public class CreateQuestionController {

    private static final Logger log = LoggerFactory.getLogger(CreateQuestionController.class);

    private final QuestionDao questionDao = QuestionDao.getInstance();

    @RequestMapping(value = "/form", method = HttpMethod.GET)
    public String questionForm(
        @RequestParam(value = "questionId", required = false) Long questionId,
        Model model,
        HttpSession session
    ) {
        if (questionId == null) {
            return "/qna/form";
        }
        Question question = questionDao.findByQuestionId(questionId);
        if (!UserSessionUtils.isSameUser(session, question.getWriter())) {
            return "redirect:/";
        }
        model.addAttribute("question", question);
        return "/qna/form";
    }

    @RequestMapping(value = "/create", method = HttpMethod.POST)
    public String createQuestion(
        @RequestParam("title") String title,
        @RequestParam("contents") String contents,
        HttpSession session
    ) {
        Question question = new Question(
            UserSessionUtils.getUserFromSession(session),
            title,
            contents
        );
        log.debug("question created : {}", question);
        questionDao.insert(question);
        return "redirect:/qna/show";
    }
}
