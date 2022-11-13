package dev.momo.api.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryDto {
    private Long categoryId;
    private String category;

    @Builder
    public CategoryDto(Long categoryId, String category){
        this.categoryId = categoryId;
        this.category = category;
    }
}
