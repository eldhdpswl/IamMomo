package dev.momo.api.board.controller;

import dev.momo.api.global.response.BaseResponse;
import dev.momo.api.global.response.BaseResponseStatus;
import dev.momo.api.board.service.serviceImpl.PostServiceImpl;
import dev.momo.api.board.dto.PostDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories/{categoryId}/questions/{questionId}/posts")
public class PostController {
    private final PostServiceImpl postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping()
    public BaseResponse<PostDto> createPost(@PathVariable("categoryId") Long categoryId,
                                            @PathVariable("questionId")Long questionId,
                                            @RequestBody PostDto dto)  {
        return new BaseResponse<>(postService.createPost(categoryId, questionId, dto));
    }

    @GetMapping()
    public BaseResponse<List<PostDto>> readAllPost(@PathVariable("categoryId")Long categoryId,
                                                  @PathVariable("questionId")Long questionId)  {
        return new BaseResponse<>(postService.readAllPost(categoryId, questionId));
    }

    @GetMapping("{postId}")
    public BaseResponse<PostDto> readPost(@PathVariable("categoryId")Long categoryId,
                                          @PathVariable("questionId")Long questionId,
                                          @PathVariable("postId")Long postId) {
        return new BaseResponse<>(postService.readPost(categoryId,questionId, postId));
    }


    @PutMapping("{postId}")
    public BaseResponse<PostDto> updatePost(@PathVariable("categoryId")Long categoryId,
                                      @PathVariable("questionId")Long questionId,
                                      @PathVariable("postId")Long postId,
                                      @RequestBody PostDto dto)  {
        return new BaseResponse<>(postService.updatePost(categoryId,questionId,postId,dto));
    }


    @DeleteMapping("{postId}")
    public BaseResponse<?> deletePost(@PathVariable("categoryId")Long categoryId,
                                            @PathVariable("questionId")Long questionId,
                                            @PathVariable("postId")Long postId)  {
        if (!postService.deletePost(categoryId, questionId, postId))
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND_POST_EXCEPTION);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

}
