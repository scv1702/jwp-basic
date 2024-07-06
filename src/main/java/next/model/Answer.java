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
public class Answer {

    @Id @GeneratedValue
    private Long answerId;

    private User writer;

    private Question question;

    @Setter
    private String contents;

    private LocalDateTime createdDate;

    public Answer() {
    }

    public Answer(Long answerId, User writer, Question question, String contents, LocalDateTime createdDate) {
        this.answerId = answerId;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        this.createdDate = createdDate;
    }

    public Answer(User writer, Question question, String contents) {
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        this.createdDate = LocalDateTime.now();
    }
}
