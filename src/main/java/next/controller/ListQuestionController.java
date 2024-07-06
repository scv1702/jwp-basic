package next.controller;

import core.bean.annotations.Controller;
import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import lombok.extern.slf4j.Slf4j;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

@Controller
@RequestMapping("/qna")
@Slf4j
public class ListQuestionController {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @Inject
    public ListQuestionController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

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
