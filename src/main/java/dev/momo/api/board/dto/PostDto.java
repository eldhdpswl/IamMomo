package dev.momo.api.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.momo.api.board.BoardStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PostDto {
    private Long postId;
    private String post;
    private BoardStatus boardStatus;
    private Instant createAt;
    private Instant updateAt;
    private QuestionDto questionDto;


    @Builder
    public PostDto(Long postId, String post, BoardStatus boardStatus, Instant createAt, Instant updateAt, QuestionDto questionDto) {
        this.postId = postId;
        this.post = post;
        this.boardStatus = boardStatus;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.questionDto = questionDto;
    }


    public PostDto convertToDto(QuestionDto questionDto) {
        PostDto postCovert = PostDto.builder()
                .postId(getPostId())
                .post(getPost())
                .questionDto(QuestionDto.builder()
                        .questionId(getQuestionDto().getQuestionId())
                        .question(getQuestionDto().getQuestion())
                        .build())
                .build();
        return postCovert;
    }

}
