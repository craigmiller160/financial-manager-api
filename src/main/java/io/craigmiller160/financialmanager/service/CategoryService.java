package io.craigmiller160.financialmanager.service;

import io.craigmiller160.financialmanager.dto.CategoryDto;
import io.craigmiller160.financialmanager.dto.CategoryListDto;
import io.craigmiller160.financialmanager.jpa.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryListDto getAllCategories() {
        // TODO finish this
        return null;
    }

    public CategoryDto createCategory(final CategoryDto category) {
        // TODO finish this
        return null;
    }

    public CategoryDto updateCategory(final long id, final CategoryDto category) {
        // TODO finish this
        return null;
    }

    public CategoryDto deleteCategory(final long id) {
        // TODO finish this
        return null;
    }

}
