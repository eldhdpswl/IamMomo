package dev.momo.api.board.service.serviceImpl;

import dev.momo.api.board.entity.Question;
import dev.momo.api.board.repository.QuestionRepository;
import dev.momo.api.board.dto.CategoryDto;
import dev.momo.api.board.entity.Category;
import dev.momo.api.board.repository.CategoryRepository;
import dev.momo.api.board.service.QuestionService;
import dev.momo.api.global.exception.CategoryNotFoundException;
import dev.momo.api.global.exception.common.InvalidParamException;
import dev.momo.api.global.exception.QuestionNotFoundException;
import dev.momo.api.board.dto.QuestionDto;
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
public class QuestionServiceImpl implements QuestionService {
    private final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    private final QuestionRepository questionRepository; /** final 안붙이면 null에러 */
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public QuestionDto createQuestion(Long categoryId, QuestionDto dto)  {

        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Category resCategory = categoryOptional.get();

        logger.info("question data : {}", dto.getQuestionId());
        Question reqQuestion = Question.builder()
                .question(dto.getQuestion())
                .build();

        Question resQuestion = this.questionRepository.save(reqQuestion);
        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .createAt(resQuestion.getCreateAt())
                .updateAt(resQuestion.getUpdateAt())
                .categoryDto(CategoryDto.builder()
                        .categoryId(resCategory.getCategoryId())
                        .category( resCategory.getCategory())
                        .build())
                .build();
        return questionDto;
    }

    @Override
    public List<QuestionDto> readAllQuestion(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        if (questionRepository.findAll().isEmpty())
            throw new QuestionNotFoundException();
        Category resCategory = categoryOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        return questionRepository.findAll().stream()
                .map(question -> QuestionDto.builder()
                        .questionId(question.getQuestionId())
                        .question(question.getQuestion())
                        .categoryDto(categoryDto)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDto readQuestion(Long categoryId, Long questionId)  {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Category resCategory = categoryOptional.get();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();

        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        Question resQuestion = questionOptional.get();

        QuestionDto questionDto = QuestionDto.builder()
                .questionId(resQuestion.getQuestionId())
                .question(resQuestion.getQuestion())
                .createAt(resQuestion.getCreateAt()) //todo : null값 해결 필요 - 부모클래스의 값을 가져오는 방법
                .updateAt(resQuestion.getUpdateAt())
                .categoryDto(categoryDto)
                .build();
        return questionDto;
    }

    @Override
    @Transactional
    public boolean updateQuestion(Long categoryId, Long questionId, QuestionDto dto)  {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

       if (!questionRepository.existsById(questionId))
           throw new QuestionNotFoundException(); //질문 존재여부확인

        Optional<Question> questionOptional = questionRepository.findById(questionId);
       if (!questionOptional.get().getQuestionId().equals(questionId)) //저장된 값이 맞는지 확인 여부
            throw new InvalidParamException();

       Question reqQuestion = Question.builder()
               .questionId(questionOptional.get().getQuestionId())
               .question( dto.getQuestion()== null ? questionOptional.get().getQuestion() : dto.getQuestion())
               .build();
       this.questionRepository.save(reqQuestion);
       return true;
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long categoryId, Long questionId)  {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        if (!questionRepository.existsById(questionId))
            throw new QuestionNotFoundException();

        Optional<Question>questionOptional = questionRepository.findById(questionId);
        questionRepository.delete(questionOptional.get());
        return true;
    }
}
