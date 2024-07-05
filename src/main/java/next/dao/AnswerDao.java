package next.dao;

import core.bean.annotations.Inject;
import core.bean.annotations.Repository;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.converter.LocalDateTimeConverter;
import next.model.Answer;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class AnswerDao {

    private final JdbcTemplate jdbcTemplate;
    private QuestionDao questionDao;
    private UserDao userDao;

    @Inject
    public AnswerDao(JdbcTemplate jdbcTemplate, QuestionDao questionDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.questionDao = questionDao;
        this.userDao = userDao;
    }

    private static final String SELECT =
        "SELECT A.answerId, A.contents ansContents, A.createdDate ansCreatedDate, A.questionId, " +
            "U2.userId qaUserId " +
            "FROM QUESTIONS Q "+
            "LEFT JOIN USERS U ON Q.writer=U.userId " +
            "JOIN ANSWERS A ON Q.questionId=A.questionId " +
            "JOIN USERS U2 ON A.writer=U2.userId";

    private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

    private final RowMapper<Answer> mapper = (ResultSet rs) -> new Answer(
        rs.getLong("answerId"),
        userDao.findByUserId(rs.getString("qaUserId")),
        questionDao.findByQuestionId(
            rs.getLong("questionId")
        ),
        rs.getString("ansContents"),
        localDateTimeConverter.fromString(rs.getString("ansCreatedDate"))
    );

    public void insert(Answer answer) {
        jdbcTemplate.insert(
            answer,
        "INSERT INTO ANSWERS VALUES (NULL, ?, ?, ?, ?)",
            answer.getWriter().getUserId(),
            answer.getContents(),
            answer.getCreatedDate(),
            answer.getQuestion().getQuestionId()
        );
        jdbcTemplate.update(
            "UPDATE QUESTIONS SET countOfAnswer = countOfAnswer + 1 WHERE questionId=?",
            answer.getQuestion().getQuestionId()
        );
    }

    public void update(Answer answer) {
        jdbcTemplate.update(
        "UPDATE ANSWERS SET contents=?, createdDate=? WHERE questionId=?",
            answer.getContents(),
            answer.getCreatedDate(),
            answer.getQuestion().getQuestionId()
        );
    }

    public Optional<Answer> findByAnswerId(Long answerId) {
        return jdbcTemplate.selectOne(SELECT + " WHERE A.answerId=?", mapper, answerId);
    }

    public List<Answer> findByQuestionId(Long questionId) {
        return jdbcTemplate.select(SELECT + " WHERE Q.questionId=?", mapper, questionId);
    }

    public void delete(Long answerId) {
        jdbcTemplate.update(
            "UPDATE QUESTIONS SET countOfAnswer = countOfAnswer - 1 " +
                "WHERE questionId = (SELECT questionId FROM ANSWERS WHERE answerId = ? LIMIT 1)",
            answerId
        );
        jdbcTemplate.delete("DELETE FROM ANSWERS WHERE answerId=?", answerId);
    }

    public List<Answer> findByWriterIsNotAndQuestionId(String userId, Long questionId) {
        return jdbcTemplate.select(
            SELECT + " WHERE A.writer!=? AND Q.questionId=?",
            mapper,
            userId,
            questionId
        );
    }
}
