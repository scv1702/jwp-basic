package next.controller;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/qna")
public class ListQuestionController {

    private static final Logger log = LoggerFactory.getLogger(ListQuestionController.class);
    private QuestionDao questionDao = new QuestionDao();
    private AnswerDao answerDao = new AnswerDao();

    @RequestMapping(value = "/show", method = HttpMethod.GET)
    public String list(
        @RequestParam(value = "questionId", required = false) Long questionId,
        Model model
    ) {
        if (questionId != null) {
            model.addAttribute("question", questionDao.findByQuestionId(questionId));
            model.addAttribute("answers", answerDao.findByQuestionId(questionId));
            return "/qna/show";
        }
        return "redirect:/";
    }
}
