/*
 *     Financial Manager API
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.financialmanager.integration;

import io.craigmiller160.apitestprocessor.body.Json;
import io.craigmiller160.financialmanager.dto.CategoryDto;
import io.craigmiller160.financialmanager.dto.CategoryListDto;
import io.craigmiller160.financialmanager.jpa.entity.Category;
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import io.craigmiller160.financialmanager.jpa.repository.CategoryRepository;
import io.craigmiller160.financialmanager.jpa.repository.TransactionRepository;
import io.craigmiller160.financialmanager.testutils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CategoryControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    private Category category1;
    private Category category2;
    private Transaction txn1;
    private Transaction txn2;

    @BeforeEach
    public void setup() {
        category1 = categoryRepo.save(new Category(0L, "First", ""));
        category2 = categoryRepo.save(new Category(0L, "Second", ""));
        txn1 = transactionRepo.save(TestData.createTransaction(1L, category1.getId()));
        txn2 = transactionRepo.save(TestData.createTransaction(2L, category2.getId()));
    }

    @AfterEach
    public void cleanup() {
        transactionRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    public void test_getAllCategories() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setPath("/categories");
            });
        }).convert(CategoryListDto.class);

        assertEquals(2, result.categories().size());
        assertEquals(category1.toDto(), result.categories().get(0));
        assertEquals(category2.toDto(), result.categories().get(1));
    }

    @Test
    public void test_createCategory() {
        final var payload = new CategoryDto(0L, "NewCategory", "");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setMethod(HttpMethod.POST);
                requestConfig.setPath("/categories");
                requestConfig.setBody(new Json(payload));
            });
        }).convert(CategoryDto.class);

        assertEquals("NewCategory", result.name());

        final var entity = categoryRepo.findById(result.id()).get();
        assertEquals(result.toEntity(), entity);
    }

    @Test
    public void test_updateCategory() {
        final var payload = new CategoryDto(0L, "UpdatedCategory", "");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setMethod(HttpMethod.PUT);
                requestConfig.setPath(String.format("/categories/%d", category1.getId()));
                requestConfig.setBody(new Json(payload));
            });
        }).convert(CategoryDto.class);

        assertEquals(new CategoryDto(category1.getId(), "UpdatedCategory", ""), result);

        final var entity = categoryRepo.findById(category1.getId()).get();
        assertEquals(result.toEntity(), entity);
    }

    @Test
    public void test_updateCategory_notFound() {
        final var payload = new CategoryDto(0L, "UpdatedCategory", "");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setMethod(HttpMethod.PUT);
                requestConfig.setPath(String.format("/categories/%d", category2.getId() + 1));
                requestConfig.setBody(new Json(payload));
            });
            apiConfig.response(responseConfig -> {
                responseConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "No category with id");
    }

    @Test
    public void test_deleteCategory() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setMethod(HttpMethod.DELETE);
                requestConfig.setPath(String.format("/categories/%d", category1.getId()));
            });
        }).convert(CategoryDto.class);

        assertEquals(category1.toDto(), result);
        final var categories = categoryRepo.findAll();
        assertEquals(1, categories.size());
        assertEquals(category2, categories.get(0));

        final var txn1Db = transactionRepo.findById(txn1.getId()).get();
        assertNull(txn1Db.getCategoryId());

        final var txn2Db = transactionRepo.findById(txn2.getId()).get();
        assertEquals(category2.getId(), txn2Db.getCategoryId());
    }

    @Test
    public void test_deleteCategory_notFound() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setMethod(HttpMethod.DELETE);
                requestConfig.setPath(String.format("/categories/%d", category2.getId() + 1));
            });
            apiConfig.response(responseConfig -> {
                responseConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "No category with id");
    }

    @Test
    public void test_getCategory() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setPath(String.format("/categories/%d", category1.getId()));
            });
        }).convert(CategoryDto.class);

        assertEquals(category1.toDto(), result);
    }

    @Test
    public void test_getCategory_notFound() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setPath(String.format("/categories/%d", category2.getId() + 1));
            });
            apiConfig.response(responseConfig -> {
                responseConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "No category with id");
    }

}
