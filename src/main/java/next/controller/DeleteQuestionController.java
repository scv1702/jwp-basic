package next.controller;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.model.User;
import next.service.QuestionService;
import next.util.UserSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
public class DeleteQuestionController {

    private static final Logger log = LoggerFactory.getLogger(DeleteQuestionController.class);

    private final QuestionService questionService = QuestionService.getInstance();

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
            questionService.delete(loginedUser, questionId);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "/error";
        }
        return "redirect:/";
    }
}
