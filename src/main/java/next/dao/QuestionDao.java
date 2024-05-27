package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.Question;

import java.util.List;

public class QuestionDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(Question question) {
        jdbcTemplate.insert(
            question,
        "INSERT INTO QUESTIONS VALUES (NULL, ?, ?, ?, ?, ?)",
            question.getWriter(),
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate(),
            question.getCountOfAnswer()
        );
    }

    public void update(Question question) {
        jdbcTemplate.update(
        "UPDATE QUESTIONS SET writer=?, title=?, contents=?, createdDate=?, countOfAnswer=? WHERE questionId=?",
            question.getWriter(),
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate(),
            question.getCountOfAnswer(),
            question.getQuestionId()
        );
    }

    public List<Question> findAll() {
        return jdbcTemplate.select(
            Question.class,
            "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS"
        );
    }

    public Question findByQuestionId(Long questionId) {
        return jdbcTemplate.selectOne(
            Question.class,
            "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS WHERE questionId=?",
            String.valueOf(questionId)
        ).orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));
    }
}
