package next.service;

import core.context.annotations.Inject;
import core.context.annotations.Service;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @Inject
    public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public List<Question> findAll() {
        return questionDao.findAll();
    }

    public Question findByQuestionId(Long questionId) {
        return questionDao.findByQuestionId(questionId);
    }

    public void delete(User loginedUser, Long questionId) {
        Question question = questionDao.findByQuestionId(questionId);
        if (!loginedUser.isSameUser(question.getWriter())) {
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
