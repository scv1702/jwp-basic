package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.converter.LocalDateTimeConverter;
import next.model.Answer;
import next.model.User;

import java.sql.ResultSet;
import java.util.List;

public class AnswerDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

    private static final String SELECT =
        "SELECT A.answerId, A.contents ansContents, A.createdDate ansCreatedDate," +
        "U2.userId qaUserId, U2.name qaName, U2.email qaEmail " +
        "FROM QUESTIONS Q "+
        "LEFT JOIN USERS U ON Q.writer=U.userId " +
        "JOIN ANSWERS A ON Q.questionId=A.questionId " +
        "JOIN USERS U2 ON A.writer=U2.userId";

    private final RowMapper<Answer> mapper = (ResultSet rs) -> new Answer(
        rs.getLong("answerId"),
        new User(
            rs.getString("qaUserId"),
            rs.getString("qaName"),
            rs.getString("qaEmail")
        ),
        null,
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
    }

    public void update(Answer answer) {
        jdbcTemplate.update(
        "UPDATE ANSWERS SET contents=?, createdDate=? WHERE questionId=?",
            answer.getContents(),
            answer.getCreatedDate(),
            answer.getQuestion().getQuestionId()
        );
    }

    public List<Answer> findByQuestionId(Long questionId) {
        return jdbcTemplate.select(SELECT + " WHERE Q.questionId=?", mapper, questionId);
    }
}
