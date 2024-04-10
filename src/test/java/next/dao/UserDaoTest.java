package next.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.ConnectionManager;
import next.model.User;

public class UserDaoTest {
    UserDao userDao = new UserDao();

    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void create() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    public void update() {
        User expected = new User("userId", "password", "name", "sanjigi@email.com");
        userDao.insert(expected);

        userDao.update(new User("userId", "password2", "name2", "sanjigi@email.com"));

        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals("password2", actual.getPassword());
        assertEquals("name2", actual.getName());
    }

    @Test
    public void findAll() {
        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }

    @Test
    public void findByUserId() {
        User expected = new User("userId", "password", "name", "sanjigi@email.com");
        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());

        assertEquals(expected, actual);
    }
}