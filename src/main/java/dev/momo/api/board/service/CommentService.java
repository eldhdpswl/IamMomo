package dev.momo.api.board.service;

import dev.momo.api.board.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long categoryId, Long questionId,  Long postId, CommentDto dto);
    List<CommentDto> readAllComment(Long categoryId, Long questionId,  Long postId);
    CommentDto updateComment(Long categoryId, Long questionId,  Long postId, Long commentId, CommentDto dto);
    boolean deleteComment(Long categoryId, Long questionId, Long postId, Long commentId);
}
