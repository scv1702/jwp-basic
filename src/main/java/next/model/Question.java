package next.model;

import core.jdbc.annotations.GeneratedValue;
import core.jdbc.annotations.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class Question {

    @Id @GeneratedValue
    private Long questionId;

    private User writer;

    @Setter
    private String title;

    @Setter
    private String contents;

    private LocalDateTime createdDate;

    private Integer countOfAnswer;

    public Question() {
    }

    public Question(Long questionId, User writer, String title, String contents, LocalDateTime createdDate, Integer countOfAnswer) {
        this.questionId = questionId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.countOfAnswer = countOfAnswer;
    }

    public Question(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = LocalDateTime.now();
        this.countOfAnswer = 0;
    }
}
