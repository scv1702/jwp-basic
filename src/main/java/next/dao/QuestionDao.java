package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.converter.LocalDateTimeConverter;
import next.model.Question;
import next.model.User;

import java.sql.ResultSet;
import java.util.List;

public class QuestionDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private static final String SELECT = "SELECT Q.questionId, Q.title, Q.contents, Q.createdDate, Q.countOfAnswer, " +
        "U.userId, U.name, U.email " +
        "FROM QUESTIONS Q "+
        "LEFT JOIN USERS U ON Q.writer=U.userId";

    private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

    private final RowMapper<Question> mapper = (ResultSet rs) -> new Question(
        rs.getLong("questionId"),
        new User(
            rs.getString("userId"),
            rs.getString("name"),
            rs.getString("email")
        ),
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
