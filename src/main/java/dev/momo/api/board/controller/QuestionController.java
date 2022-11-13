package dev.momo.api.board.controller;

import dev.momo.api.global.response.BaseResponse;
import dev.momo.api.board.service.serviceImpl.QuestionServiceImpl;
import dev.momo.api.board.dto.QuestionDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories/{categoryId}/questions")
public class QuestionController {
    private final QuestionServiceImpl questionService;

    public QuestionController(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @PostMapping()
    public BaseResponse<QuestionDto> createQuestion(@PathVariable("categoryId")Long categoryId,
                                                    @RequestBody QuestionDto dto){

        return new BaseResponse<>(questionService.createQuestion(categoryId, dto));
    }

    @GetMapping()
    public BaseResponse<List<QuestionDto>> readAllQuestion(@PathVariable("categoryId")Long categoryId)  {
        return new BaseResponse<>(questionService.readAllQuestion(categoryId));
    }

    @GetMapping("{questionId}")
    public BaseResponse<QuestionDto> readQuestion(@PathVariable("categoryId")Long categoryId,
                                                  @PathVariable("questionId")Long questionId)  {
        return new BaseResponse<>(questionService.readQuestion(categoryId, questionId));
    }

    @PutMapping("{questionId}")
    public BaseResponse<?>updateQuestion(@PathVariable("categoryId")Long categoryId,
                                         @PathVariable("questionId")Long questionId,
                                         @RequestBody QuestionDto dto) {
        return new BaseResponse<>(questionService.updateQuestion(categoryId, questionId, dto));
    }

    @DeleteMapping("{questionId}")
    public BaseResponse<?>deleteQuestion(@PathVariable("categoryId")Long categoryId,
                                         @PathVariable("questionId")Long questionId) {
        return new BaseResponse<>(questionService.deleteQuestion(categoryId, questionId));
    }

}
