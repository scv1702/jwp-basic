package next.controller;

import core.bean.annotations.Controller;
import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import lombok.extern.slf4j.Slf4j;
import next.model.User;
import next.service.QuestionService;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/qna")
@Slf4j
public class DeleteQuestionController {

    private final QuestionService questionService;

    @Inject
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
            questionService.delete(loginedUser, questionId);
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "/error";
        }
        return "redirect:/";
    }
}
