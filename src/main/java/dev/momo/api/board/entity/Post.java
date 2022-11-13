package dev.momo.api.board.entity;

import dev.momo.api.board.BoardStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "post")
public class Post  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;
    private String post;
    private BoardStatus boardStatus;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Category.class
    )
    @JoinColumn(name = "category_Id")
    private Category category;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Question.class
    )
    @JoinColumn(name = "question_Id")
    private Question question;

    protected Post() {
    }

    @Builder
    public Post(Long postId, String post, BoardStatus boardStatus, Category category, Question question) {
        this.postId = postId;
        this.post = post;
        this.boardStatus = boardStatus;
        this.category = category;
        this.question = question;
    }
}
