package next.api;

import core.context.annotations.Controller;
import core.http.HttpMethod;
import core.http.ResponseEntity;
import core.web.annotations.RequestMapping;
import next.dao.QuestionDao;
import next.model.Question;
import next.util.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/api/qna")
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
    private QuestionDao questionDao = new QuestionDao();

    @RequestMapping(value = "/list", method = HttpMethod.GET)
    public ResponseEntity<ApiResult.ApiSuccessResult<List<Question>>> list() {
        List<Question> questions = questionDao.findAll();
        return ResponseEntity.ok().body(ApiResult.success(questions));
    }
}
