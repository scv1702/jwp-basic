package next.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.web.Controller;
import core.web.RequestMapping;
import core.web.RequestMethod;
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

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String list(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
        req.setAttribute("questions", questionDao.findAll());
        return "/qna/show";
    }
}
