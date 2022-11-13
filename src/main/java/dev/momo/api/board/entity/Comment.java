package dev.momo.api.board.entity;

import dev.momo.api.board.BoardStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;
    private String comment;
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

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Post.class
    )
    @JoinColumn(name = "post_Id")
    private Post post;

    protected Comment(){

    }

    @Builder
    public Comment(Long commentId, String comment, BoardStatus boardStatus, Category category, Question question, Post post) {
        this.commentId = commentId;
        this.comment = comment;
        this.boardStatus = boardStatus;
        this.category = category;
        this.question = question;
        this.post = post;
    }
}
