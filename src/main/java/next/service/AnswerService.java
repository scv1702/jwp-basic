package next.service;

import next.dao.AnswerDao;
import next.model.Answer;
import next.model.User;

import java.util.Optional;

public class AnswerService {

    private static AnswerService instance;

    private AnswerService() {
    }

    public static AnswerService getInstance() {
        if (instance == null) {
            return new AnswerService();
        }
        return instance;
    }

    private final AnswerDao answerDao = AnswerDao.getInstance();
    private final QuestionService questionService = QuestionService.getInstance();

    public Answer insert(User writer, Long questionId, String contents) {
        Answer answer = new Answer(
            writer,
            questionService.findByQuestionId(questionId),
            contents
        );
        answerDao.insert(answer);
        return findByAnswerId(answer.getAnswerId())
            .orElseThrow(() -> new IllegalStateException("답변이 등록되지 않았습니다."));
    }

    public void delete(User loginedUser, Long answerId) {
        Answer answer = findByAnswerId(answerId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 답변입니다."));
        if (!answer.getWriter().isSameUser(loginedUser)) {
            throw new IllegalArgumentException("본인이 작성한 답변만 삭제할 수 있습니다.");
        }
        answerDao.delete(answerId);
    }

    public Optional<Answer> findByAnswerId(Long answerId) {
        return answerDao.findByAnswerId(answerId);
    }
}
