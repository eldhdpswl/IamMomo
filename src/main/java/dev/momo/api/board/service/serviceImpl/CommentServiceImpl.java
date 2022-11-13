package dev.momo.api.board.service.serviceImpl;

import dev.momo.api.board.BoardStatus;
import dev.momo.api.board.dto.CommentDto;
import dev.momo.api.board.dto.PostDto;
import dev.momo.api.board.entity.Category;
import dev.momo.api.board.entity.Comment;
import dev.momo.api.board.entity.Post;
import dev.momo.api.board.entity.Question;
import dev.momo.api.board.repository.CategoryRepository;
import dev.momo.api.board.repository.CommentRepository;
import dev.momo.api.board.repository.PostRepository;
import dev.momo.api.board.repository.QuestionRepository;
import dev.momo.api.board.service.CommentService;
import dev.momo.api.global.exception.CategoryNotFoundException;
import dev.momo.api.global.exception.CommentNotFoundException;
import dev.momo.api.global.exception.PostNotFoundException;
import dev.momo.api.global.exception.QuestionNotFoundException;
import dev.momo.api.global.exception.common.InvalidParamException;
import dev.momo.api.global.exception.common.IsDeletedException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final Logger logger =  LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;


    @Override
    @Transactional
    public CommentDto createComment(Long categoryId, Long questionId, Long postId, CommentDto dto) {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        if (!postRepository.existsById(postId))
            throw new PostNotFoundException();


        Optional<Post> postOptional = postRepository.findById(postId);
        Post reqPost = postOptional.get();

        //삭제된 게시글일 경우 생성 불가 처리
        if (reqPost.getBoardStatus() == BoardStatus.DELETED)
            throw new IsDeletedException();

        //todo:게시글이 수정될경우 수정된 값과 매핑하여 댓글이 달려야 하는데 이걸 어떻게 하지...?
     //   if (reqPost.getBoardStatus() == BoardStatus.UPDATED)

        Comment reqComment = Comment.builder()
                .comment(dto.getComment())
                .boardStatus(BoardStatus.NOT_CHANGED)
                .build();
        Comment resComment = commentRepository.save(reqComment);
        CommentDto commentDto = CommentDto.builder()
                .commentId(resComment.getCommentId())
                .comment(resComment.getComment())
                .boardStatus(resComment.getBoardStatus())
                .createAt(resComment.getCreateAt())
                .updateAt(resComment.getUpdateAt())
                .postDto(PostDto.builder()
                        .postId(reqPost.getPostId())
                        .post(reqPost.getPost())
                        .boardStatus(reqPost.getBoardStatus())
                        .build())
                .build();
        return commentDto;
    }

    @Override
    public List<CommentDto> readAllComment(Long categoryId, Long questionId, Long postId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postRepository.existsById(postId))
            throw new PostNotFoundException();


//        Category resCategory = categoryOptional.get();
//        Question resQuestion = questionOptional.get();
        Post resPost = postOptional.get();

        PostDto postDto = PostDto.builder()
                .postId(resPost.getPostId())
                .post(resPost.getPost())
                .boardStatus(resPost.getBoardStatus())
                .build();
        if (commentRepository.findAll().isEmpty())
            throw new CommentNotFoundException();

        return commentRepository.findAll().stream()
                .map(comment -> CommentDto.builder()
                        .commentId(comment.getCommentId())
                        .comment(comment.getComment())
                        .boardStatus(comment.getBoardStatus())
                        .createAt(comment.getCreateAt())
                        .updateAt(comment.getUpdateAt())
                                .postDto(postDto)
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long categoryId, Long questionId, Long postId, Long commentId, CommentDto dto) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postRepository.existsById(postId))
            throw new PostNotFoundException();

//        Category resCategory = categoryOptional.get();
//        Question resQuestion = questionOptional.get();
        Post resPost = postOptional.get();

        PostDto postDto = PostDto.builder()
                .postId(resPost.getPostId())
                .post(resPost.getPost())
                .boardStatus(resPost.getBoardStatus())
                .build();

        //삭제된 게시글일 경우 댓글수정 불가
        if (resPost.getBoardStatus() == BoardStatus.DELETED)
            throw new IsDeletedException();

        // 댓글 같은값인지 확인
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.get().getCommentId().equals(commentId))
            throw new InvalidParamException();

        // 댓글 상태 수정
        Comment reqComment = Comment.builder()
                .commentId(commentOptional.get().getCommentId())
                .comment(dto.getComment() == null? commentOptional.get().getComment() : dto.getComment())
                .boardStatus(BoardStatus.UPDATED)
                .build();
        this.commentRepository.save(reqComment);


        CommentDto commentDto = CommentDto.builder()
                .commentId(reqComment.getCommentId())
                .comment(reqComment.getComment())
                .boardStatus(reqComment.getBoardStatus())
                .createAt(reqComment.getCreateAt())
                .updateAt(reqComment.getUpdateAt())
                .postDto(postDto)
                .build();
        return commentDto;
    }

    @Override
    @Transactional
    public boolean deleteComment(Long categoryId, Long questionId, Long postId, Long commentId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Category resCategory = categoryOptional.get();
        Question resQuestion = questionOptional.get();

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.get().getCommentId().equals(commentId))
            throw new InvalidParamException();

        Comment changeCommentPost = Comment.builder()
                .commentId(commentOptional.get().getCommentId())
                .comment(commentOptional.get().getComment())
                .boardStatus(BoardStatus.DELETED)
                .build();
        this.commentRepository.save(changeCommentPost);
        return true;
    }
}
