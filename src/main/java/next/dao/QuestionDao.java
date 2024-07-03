package next.dao;

import core.bean.annotations.Inject;
import core.bean.annotations.Repository;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.converter.LocalDateTimeConverter;
import next.model.Question;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class QuestionDao {

    private UserDao userDao;

    @Inject
    public QuestionDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private static final String SELECT = "SELECT Q.questionId, Q.title, Q.contents, Q.createdDate, Q.countOfAnswer, " +
        "U.userId " +
        "FROM QUESTIONS Q "+
        "LEFT JOIN USERS U ON Q.writer=U.userId";

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

    private final RowMapper<Question> mapper = (ResultSet rs) -> new Question(
        rs.getLong("questionId"),
        userDao.findByUserId(rs.getString("userId")),
        rs.getString("title"),
        rs.getString("contents"),
        localDateTimeConverter.fromString(rs.getString("createdDate")),
        rs.getInt("countOfAnswer")
    );

    public void insert(Question question) {
        jdbcTemplate.insert(
            question,
        "INSERT INTO QUESTIONS VALUES (NULL, ?, ?, ?, ?, ?)",
            question.getWriter().getUserId(),
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate(),
            question.getCountOfAnswer()
        );
    }

    public void update(Question question) {
        jdbcTemplate.update(
        "UPDATE QUESTIONS SET title=?, contents=?, createdDate=?, countOfAnswer=? WHERE questionId=?",
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate(),
            question.getCountOfAnswer(),
            question.getQuestionId()
        );
    }

    public void delete(Long questionId) {
        jdbcTemplate.update(
            "DELETE FROM QUESTIONS WHERE questionId=?",
            questionId
        );
    }

    public List<Question> findAll() {
        return jdbcTemplate.select(SELECT, mapper);
    }

    public Question findByQuestionId(Long questionId) {
        return jdbcTemplate.selectOne(
            SELECT + " WHERE Q.questionId=?",
            mapper,
            questionId
        ).orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));
    }
}
