package next.controller;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.User;
import next.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
public class DeleteQuestionController {

    private static final Logger log = LoggerFactory.getLogger(DeleteQuestionController.class);

    private QuestionService questionService = new QuestionService(new QuestionDao(), new AnswerDao());

    public DeleteQuestionController() {
    }

    public DeleteQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(value = "/delete", method = HttpMethod.POST)
    public String deleteQuestion(
        @RequestParam("questionId") Long questionId,
        HttpSession session,
        Model model
    ) {
        User loginedUser = UserSessionUtils.getUserFromSession(session);
        if (loginedUser == null) {
            return "/user/login";
        }
        try {
            questionService.deleteQuestion(loginedUser, questionId);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "/error";
        }
        return "redirect:/";
    }
}
