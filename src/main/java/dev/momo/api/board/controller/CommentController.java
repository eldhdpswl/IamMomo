package dev.momo.api.board.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import dev.momo.api.board.dto.CommentDto;
import dev.momo.api.board.service.serviceImpl.CommentServiceImpl;
import dev.momo.api.global.response.BaseResponse;
import dev.momo.api.global.response.BaseResponseStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories/{categoryId}/questions/{questionId}/posts/{postId}/comments")
public class CommentController {
    private final CommentServiceImpl commentService;


    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public BaseResponse<CommentDto> createComment(@PathVariable("categoryId") Long categoryId,
                                                  @PathVariable("questionId")Long questionId,
                                                  @PathVariable("postId")Long postId,
                                                  @RequestBody CommentDto dto){

        return new BaseResponse<>(commentService.createComment(categoryId, questionId, postId, dto));
    }
    @GetMapping()
    public BaseResponse<List<CommentDto>> readAllComment(@PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("questionId")Long questionId,
                                                         @PathVariable("postId")Long postId){

        return new BaseResponse<>(commentService.readAllComment(categoryId, questionId, postId));
    }

    @PutMapping("{commentId}")
    public BaseResponse<CommentDto> updateComment(@PathVariable("categoryId") Long categoryId,
                                                  @PathVariable("questionId")Long questionId,
                                                  @PathVariable("postId")Long postId,
                                                  @PathVariable("commentId")Long commentId,
                                                  @RequestBody CommentDto dto){
        return new BaseResponse<>(commentService.updateComment(categoryId, questionId,commentId, postId, dto));
    }

    @DeleteMapping("{commentId}")
    public BaseResponse<?> deleteComment(@PathVariable("categoryId") Long categoryId,
                                         @PathVariable("questionId")Long questionId,
                                         @PathVariable("postId")Long postId,
                                         @PathVariable("commentId")Long commentId){

        if (!commentService.deleteComment(categoryId,questionId,postId,commentId))
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_COMMENT_EXCEPTION);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }



}
