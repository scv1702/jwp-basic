package next.dao;

import core.ApplicationContext;
import core.bean.annotations.Inject;
import next.model.Question;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionDaoTest {

    @Inject
    private UserDao userDao;

    @Inject
    private QuestionDao questionDao;

    User writer = new User("scv1702", "password", "name", "email");

    @BeforeEach
    public void setup() {
        ApplicationContext ac = new ApplicationContext("next");

        userDao = ac.getBean(UserDao.class);
        questionDao = ac.getBean(QuestionDao.class);

        userDao.insert(writer);
    }

    @Test
    public void create() {
        Question expected = new Question(writer, "title", "contents");
        questionDao.insert(expected);

        Question actual = questionDao.findByQuestionId(expected.getQuestionId());
        assertEquals(expected, actual);
    }

    @Test
    public void update() {
        Question question = new Question(writer, "title", "contents");
        questionDao.insert(question);

        question.setTitle("title2");
        question.setContents("contents2");

        questionDao.update(question);

        Question actual = questionDao.findByQuestionId(question.getQuestionId());
        assertEquals("title2", actual.getTitle());
        assertEquals("contents2", actual.getContents());
    }

    @Test
    public void findAll() {
        List<Question> questions = questionDao.findAll();
        assertEquals(8, questions.size());
    }

    @Test
    public void findByQuestionId() {
        Question expected = new Question(writer, "title", "contents");
        questionDao.insert(expected);

        Question actual = questionDao.findByQuestionId((expected.getQuestionId()));

        assertEquals(expected, actual);
    }
}