package next.dao;

import core.bean.annotations.Inject;
import core.bean.annotations.Repository;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        jdbcTemplate.insert(
            user,
        "INSERT INTO USERS VALUES (?, ?, ?, ?)",
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.update(
        "UPDATE USERS SET USERID=?, PASSWORD=?, NAME=?, EMAIL=? WHERE USERID=?",
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getUserId()
        );
    }

    public List<User> findAll() {
        return jdbcTemplate.select(
            User.class,
            "SELECT userId, password, name, email FROM USERS"
        );
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.selectOne(
            User.class,
            "SELECT userId, password, name, email FROM USERS WHERE userid=?",
            userId
        ).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
