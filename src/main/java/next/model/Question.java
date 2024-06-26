package next.model;

import core.jdbc.annotations.GeneratedValue;
import core.jdbc.annotations.Id;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {

    @Id @GeneratedValue
    private Long questionId;
    private User writer;
    private String title;
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question = (Question) o;

        return Objects.equals(questionId, question.questionId ) &&
                Objects.equals(writer, question.writer) &&
                Objects.equals(title, question.title) &&
                Objects.equals(contents, question.contents) &&
                Objects.equals(createdDate, question.createdDate) &&
                Objects.equals(countOfAnswer, question.countOfAnswer);
    }

    @Override
    public String toString() {
        return "[questionId=" + questionId + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", createdDate=" + createdDate + ", countOfAnswer=" + countOfAnswer + "]";
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Integer getCountOfAnswer() {
        return countOfAnswer;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
