package next.model;

import core.jdbc.annotations.GeneratedValue;
import core.jdbc.annotations.Id;

import java.time.LocalDateTime;
import java.util.Objects;

public class Answer {

    @Id @GeneratedValue
    private Long answerId;
    private User writer;
    private Question question;
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        return Objects.equals(answerId, answer.answerId) &&
                Objects.equals(writer, answer.writer) &&
                Objects.equals(contents, answer.contents) &&
                Objects.equals(createdDate, answer.createdDate);
    }

    @Override
    public String toString() {
        return "[answerId=" + answerId +
            ", writer=" + writer +
            ", questions=" + question +
            ", contents=" + contents +
            ", createdDate=" + createdDate +
            "]";
    }

    public Long getAnswerId() {
        return answerId;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
