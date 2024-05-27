package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.Question;

import java.util.List;

public class QuestionDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(Question question) {
        jdbcTemplate.insert(
        "INSERT INTO QUESTIONS VALUES (?, ?, ?, ?, ?, ?)",
            question.getQuestionId(),
            question.getWriter(),
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate().toString(),
            question.getCountOfAnswer()
        );
    }

    public void update(Question question) {
        jdbcTemplate.update(
        "UPDATE QUESTIONS SET questionId=?, writer=?, title=?, contents=?, createdDate=?, countOfAnswer=? WHERE questionId=?",
            question.getQuestionId(),
            question.getWriter(),
            question.getTitle(),
            question.getContents(),
            question.getCreatedDate().toString(),
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
