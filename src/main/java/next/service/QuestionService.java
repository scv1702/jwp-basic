package next.service;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import java.util.List;

public class QuestionService {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public void deleteQuestion(User loginedUser, Long questionId) {
        Question question = questionDao.findByQuestionId(questionId);
        if (!loginedUser.equals(question.getWriter())) {
            throw new IllegalStateException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
        }
        String writer = question.getWriter().getUserId();
        List<Answer> answers = answerDao.findByWriterIsNotAndQuestionId(writer, questionId);
        if (!answers.isEmpty()) {
            throw new IllegalStateException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
        }
        questionDao.delete(questionId);
    }
}
