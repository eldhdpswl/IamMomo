package dev.momo.api.board.service.serviceImpl;
import dev.momo.api.board.BoardStatus;
import dev.momo.api.board.dto.PostDto;
import dev.momo.api.board.repository.PostRepository;
import dev.momo.api.board.dto.CategoryDto;
import dev.momo.api.board.entity.Category;
import dev.momo.api.board.repository.CategoryRepository;
import dev.momo.api.board.service.PostService;
import dev.momo.api.global.exception.CategoryNotFoundException;
import dev.momo.api.global.exception.common.InvalidParamException;
import dev.momo.api.global.exception.PostNotFoundException;
import dev.momo.api.global.exception.QuestionNotFoundException;
import dev.momo.api.board.entity.Post;
import dev.momo.api.board.dto.QuestionDto;
import dev.momo.api.board.entity.Question;
import dev.momo.api.board.repository.QuestionRepository;
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
public class PostServiceImpl implements PostService {
    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostDto createPost(Long categoryId, Long questionId, PostDto dto)  {
      if (!categoryRepository.existsById(categoryId))
          throw new CategoryNotFoundException();

      if (!questionRepository.existsById(questionId))
          throw new QuestionNotFoundException();

      // 카테고리 정보 가져오기
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Category reqCategory = categoryOptional.get();
        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(reqCategory.getCategoryId())
                .category(reqCategory.getCategory())
                .build();

      // 질문 정보 가져오기
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        Question reqQuestion = questionOptional.get();

      // 게시글 생성
        logger.info("post data : {}", dto.getPostId());
        Post reqPost = Post.builder()
                .post(dto.getPost())
                .boardStatus(BoardStatus.NOT_CHANGED)
                .build();
        logger.info("들어온값{}",dto.getPost());
        logger.info("저장된값{}",reqPost.getPost());

        Post resPost = postRepository.save(reqPost);
        PostDto postDto = PostDto.builder()
                .postId(resPost.getPostId())
                .post(resPost.getPost())
                .boardStatus(reqPost.getBoardStatus())
                .createAt(resPost.getCreateAt())
                .updateAt(resPost.getUpdateAt())
                .questionDto(QuestionDto.builder()
                        .questionId(reqQuestion.getQuestionId())
                        .question(reqQuestion.getQuestion())
                        .categoryDto(categoryDto)
                        .build())
                .build();
        return postDto;
    }

    @Override
    public List<PostDto> readAllPost(Long categoryId, Long questionId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
        throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Category resCategory = categoryOptional.get();
        Question resQuestion = questionOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .categoryDto(categoryDto)
                .build();

        if (postRepository.findAll().isEmpty())
            throw new PostNotFoundException();

        return postRepository.findAll().stream()
                .map(post -> PostDto.builder()
                        .postId(post.getPostId())
                        .post(post.getPost())
                        .questionDto(questionDto)
                        .boardStatus(post.getBoardStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PostDto readPost(Long categoryId, Long questionId, Long postId) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Category resCategory = categoryOptional.get();
        Question resQuestion = questionOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .categoryDto(categoryDto)
                .build();

       Optional<Post> postOptional = postRepository.findById(postId);
       Post resPost = postOptional.get();

       PostDto postDto = PostDto.builder()
               .postId(resPost.getPostId())
               .post(resPost.getPost())
               .createAt(resPost.getCreateAt())
               .updateAt(resPost.getUpdateAt())
               .boardStatus(resPost.getBoardStatus())
               .questionDto(questionDto)
               .build();
       return postDto;
    }

    @Override
    @Transactional
    public PostDto updatePost(Long categoryId, Long questionId, Long postId, PostDto dto) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Category resCategory = categoryOptional.get();
        Question resQuestion = questionOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .categoryDto(categoryDto)
                .build();

        //파람 값으로 해당 게시글 확인
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.get().getPostId().equals(postId))
            throw new InvalidParamException();

        //최초 생성된 데이터의 update상태를 변경
        Post changeUpdatePost = Post.builder()
                .postId(postOptional.get().getPostId())
                .post(postOptional.get().getPost())
                .boardStatus(BoardStatus.UPDATED)
                .build();
        this.postRepository.save(changeUpdatePost);

        //새로 생성된 수정데이터를 생성
        Post reqPost = Post.builder()
                .post(dto.getPost()==null? postOptional.get().getPost() : dto.getPost())
                .boardStatus(changeUpdatePost.getBoardStatus())
                .build();
        Post resPost = postRepository.save(reqPost);

        PostDto postDto = PostDto.builder()
                .postId(resPost.getPostId())
                .post(resPost.getPost())
                .boardStatus(resPost.getBoardStatus())
                .createAt(resPost.getCreateAt())
                .updateAt(resPost.getUpdateAt())
                .questionDto(questionDto)
                .build();
        return postDto;
    }

    @Override
    @Transactional
    public boolean deletePost(Long categoryId, Long questionId, Long postId)  {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Category resCategory = categoryOptional.get();
        Question resQuestion = questionOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .categoryDto(categoryDto)
                .build();

        //파람 값으로 해당 게시글 확인
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.get().getPostId().equals(postId))
            throw new InvalidParamException();

        //최초 생성된 데이터의 delete상태를 변경
        Post changeUpdatePost = Post.builder()
                .postId(postOptional.get().getPostId())
                .post(postOptional.get().getPost())
                .boardStatus(BoardStatus.DELETED)
                .build();
        this.postRepository.save(changeUpdatePost);
        return true;
    }
}
