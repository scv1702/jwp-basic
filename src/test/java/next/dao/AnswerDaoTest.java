package next.dao;

import core.bean.annotations.Inject;
import core.jdbc.ConnectionManager;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnswerDaoTest {

    @Inject
    private UserDao userDao;

    @Inject
    private QuestionDao questionDao;

    @Inject
    private AnswerDao answerDao;

    User questionWriter = new User("scv1702", "password", "name", "email");
    User questionAnswer = new User("scv1703", "password", "name2", "email2");
    Question question = new Question(questionWriter, "title", "contents");

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        userDao.insert(questionWriter);
        userDao.insert(questionAnswer);
        questionDao.insert(question);
    }

    @Test
    public void create() {
        Answer expected = new Answer(questionAnswer, question, "answer");
        answerDao.insert(expected);

        assertNotNull(expected.getAnswerId());
    }

    @Test
    public void update() {
        Answer expected = new Answer(questionAnswer, question, "answer");
        answerDao.insert(expected);

        expected.setContents("answer2");
        answerDao.update(expected);

        assertEquals("answer2", expected.getContents());
    }

    @Test
    public void findByQuestionId() {
        Answer expected = new Answer(questionAnswer, question, "answer");
        answerDao.insert(expected);

        answerDao.findByQuestionId(question.getQuestionId());
    }
}