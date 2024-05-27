package next.dao;

import core.jdbc.ConnectionManager;
import next.model.Question;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

public class QuestionDaoTest {
    QuestionDao questionDao = new QuestionDao();

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void create() {
        Question expected = new Question(10L, "scv1702", "title", "contents");
        questionDao.insert(expected);

        Question actual = questionDao.findByQuestionId(10L);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void update() {
        Question expected = new Question(10L, "scv1702", "title", "contents");
        questionDao.insert(expected);

        questionDao.update(new Question(10L, "scv1702", "title2", "contents2"));

        Question actual = questionDao.findByQuestionId(10L);
        Assert.assertEquals("title2", actual.getTitle());
        Assert.assertEquals("contents2", actual.getContents());
    }

    @Test
    public void findAll() {
        List<Question> questions = questionDao.findAll();
        Assert.assertEquals(8, questions.size());
    }

    @Test
    public void findByQuestionId() {
        Question expected = new Question(10L, "scv1702", "title", "contents");
        questionDao.insert(expected);

        Question actual = questionDao.findByQuestionId(10L);

        Assert.assertEquals(expected, actual);
    }
}