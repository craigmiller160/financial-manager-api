package io.craigmiller160.financialmanager.service;

import io.craigmiller160.financialmanager.dto.CategoryDto;
import io.craigmiller160.financialmanager.dto.CategoryListDto;
import io.craigmiller160.financialmanager.jpa.entity.Category;
import io.craigmiller160.financialmanager.jpa.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Transactional
    public CategoryListDto getAllCategories() {
        final var categories = categoryRepo.findAll()
                .stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
        return new CategoryListDto(categories);
    }

    @Transactional
    public CategoryDto createCategory(final CategoryDto category) {
        final var entity = category.toEntity();
        entity.setId(null);
        return categoryRepo.save(entity).toDto();
    }

    @Transactional
    public CategoryDto updateCategory(final long id, final CategoryDto category) {
        categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No category with id %d", id)));

        final var entity = category.toEntity();
        entity.setId(id);
        return categoryRepo.save(entity).toDto();
    }

    @Transactional
    public CategoryDto deleteCategory(final long id) {
        final var category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No category with id %d", id)))
                .toDto();
        categoryRepo.deleteById(id);
        return category;
    }

    @Transactional
    public CategoryDto getCategory(final long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No category with id %d", id)))
                .toDto();
    }

}
