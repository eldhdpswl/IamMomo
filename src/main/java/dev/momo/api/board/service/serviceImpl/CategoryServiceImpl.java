package dev.momo.api.board.service.serviceImpl;

import dev.momo.api.board.dto.CategoryDto;
import dev.momo.api.board.entity.Category;
import dev.momo.api.board.repository.CategoryRepository;

import dev.momo.api.board.service.CategoryService;
import org.springframework.data.domain.Pageable;

import dev.momo.api.global.exception.CategoryNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional //트랜잭션은 데이터가 변경될 경우만 사용하기! 자원 낭비가 된다
    public CategoryDto createCategory(CategoryDto dto) {
        Category reqCategory = Category.builder()
                .category(dto.getCategory())
                .build();
        Category resCategory = categoryRepository.save(reqCategory);

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(resCategory.getCategoryId())
                .category(resCategory.getCategory())
                .build();
        return categoryDto;
    }

    @Override

    public List<CategoryDto> readAllCategory()  {
        if(categoryRepository.findAll().isEmpty())
            throw new CategoryNotFoundException();

        return categoryRepository.findAll().stream()
                .map(category -> CategoryDto.builder()
                        .categoryId(category.getCategoryId())
                        .category(category.getCategory())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public CategoryDto readCategory(Long categoryId){
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isEmpty())
            throw new CategoryNotFoundException();

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(categoryOptional.get().getCategoryId())
                .category(categoryOptional.get().getCategory())
                .build();
        return  categoryDto;
    }

    @Override
    @Transactional
    public boolean updateCategory(Long categoryId, CategoryDto dto)  {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Category reqCategory = Category.builder()
                .categoryId(categoryOptional.get().getCategoryId())
                .category(dto.getCategory()== null?  categoryOptional.get().getCategory() : dto.getCategory())
                .build();
        categoryRepository.save(reqCategory);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Optional<Category> category = categoryRepository.findById(categoryId);
        categoryRepository.delete(category.get());
        return true;
    }

    //패이징처리용 테스트 용도
    @Override
    public List<CategoryDto> readAllCategory1 (Pageable pageable) {
        List<CategoryDto> list = categoryRepository.findAll(pageable).stream()
                .map(category -> CategoryDto.builder()
                        .categoryId(category.getCategoryId())
                        .category(category.getCategory())
                        .build())
                .collect(Collectors.toList());
    return list;
    }
}
