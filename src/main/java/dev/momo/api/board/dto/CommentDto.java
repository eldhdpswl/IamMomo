package dev.momo.api.board.dto;

import dev.momo.api.board.BoardStatus;
import dev.momo.api.board.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CommentDto {
    private Long commentId;
    private String comment;
    private BoardStatus boardStatus;
    private Instant createAt;
    private Instant updateAt;
    private PostDto postDto;

    @Builder
    public CommentDto(Long commentId, String comment, BoardStatus boardStatus, Instant createAt, Instant updateAt, PostDto postDto) {
        this.commentId = commentId;
        this.comment = comment;
        this.boardStatus = boardStatus;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.postDto = postDto;
    }
//
//    public static CommentDto convertToDto(){
//
//    }
}
