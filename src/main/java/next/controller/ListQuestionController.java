package next.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.util.json.JsonConverter;
import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.RequestMethod;
import core.web.annotations.ResponseBody;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/qna")
public class ListQuestionController {

    private static final Logger log = LoggerFactory.getLogger(ListQuestionController.class);
    private QuestionDao questionDao = new QuestionDao();
    private AnswerDao answerDao = new AnswerDao();
    private JsonConverter jsonConverter = new JsonConverter();

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String getQuestions(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        if (req.getParameter("questionId") != null) {
            Long questionId = Long.valueOf(req.getParameter("questionId"));
            return jsonConverter.convertToJson(questionDao.findByQuestionId(questionId));
        }
        return jsonConverter.convertToJson(questionDao.findAll());
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String list(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        if (req.getParameter("questionId") != null) {
            Long questionId = Long.valueOf(req.getParameter("questionId"));
            req.setAttribute("question", questionDao.findByQuestionId(questionId));
            req.setAttribute("answers", answerDao.findByQuestionId(questionId));
            return "/qna/show";
        }
        return "redirect:/";
    }
}
