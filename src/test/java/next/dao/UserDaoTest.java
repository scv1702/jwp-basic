package next.dao;

import core.ApplicationContext;
import core.bean.annotations.Inject;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoTest {

    @Inject
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ApplicationContext ac = new ApplicationContext("next");

        userDao = ac.getBean(UserDao.class);
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
        User user = new User("userId", "password", "name", "sanjigi@email.com");
        userDao.insert(user);

        user.setPassword("password2");
        user.setName("name2");

        userDao.update(user);

        User actual = userDao.findByUserId(user.getUserId());
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