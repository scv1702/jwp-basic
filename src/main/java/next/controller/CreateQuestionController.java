package next.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.RequestMethod;
import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/qna")
public class CreateQuestionController {

    private static final Logger log = LoggerFactory.getLogger(CreateQuestionController.class);
    private QuestionDao questionDao = new QuestionDao();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String questionForm(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        return "/qna/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createQuestion(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        Question question = new Question(
            UserSessionUtils.getUserFromSession(req.getSession()),
            req.getParameter("title"),
            req.getParameter("contents")
        );
        log.debug("question created : {}", question);
        questionDao.insert(question);
        return "redirect:/qna/show";
    }
}
