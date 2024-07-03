package next.service;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionDao questionDao;

    @Mock
    private AnswerDao answerDao;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void findAll() {
        when(questionDao.findAll()).thenReturn(new ArrayList<>());
    }

    @Test
    void findByQuestionId() {
    }

    @Test
    void deleteQuestion() {
    }
}