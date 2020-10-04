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
import io.craigmiller160.apitestprocessor.result.ApiResult;
import io.craigmiller160.financialmanager.dto.SearchRequestDto;
import io.craigmiller160.financialmanager.dto.SearchResponseDto;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private TransactionRepository transactionRepo;

    private Category category1;
    private Transaction txn1;
    private Transaction txn2;
    private Transaction txn3;
    private Transaction txn4;

    @BeforeEach
    public void setup() {
        category1 = categoryRepo.save(new Category(0L, "Category"));
        txn1 = transactionRepo.save(TestData.createTransaction(1L, category1.getId()));
        txn1 = transactionRepo.findById(txn1.getId()).get();
        txn2 = transactionRepo.save(TestData.createTransaction(2L, category1.getId()));
        txn2 = transactionRepo.findById(txn2.getId()).get();
        txn3 = transactionRepo.save(TestData.createTransaction(3L,null));
        txn3 = transactionRepo.findById(txn3.getId()).get();
        txn4 = transactionRepo.save(TestData.createTransaction(4L, category1.getId()));
        txn4 = transactionRepo.findById(txn4.getId()).get();
    }

    @AfterEach
    public void cleanup() {
        transactionRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    public void test_searchTransactions_allParams() {
        final var searchRequest = new SearchRequestDto(
                0,
                LocalDate.now(),
                LocalDate.now().plus(4, ChronoUnit.DAYS),
                List.of(category1.getId())
        );
        final var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/transactions/search");
                reqConfig.setBody(new Json(searchRequest));
            });
        }).convert(SearchResponseDto.class);

        assertEquals(2, result.totalPages());
        assertEquals(0, result.currentPage());
        assertEquals(txn4.toDto(), result.transactions().get(0));
        assertEquals(txn2.toDto(), result.transactions().get(1));
    }

    @Test
    public void test_searchTransactions_noParams() {
        final var searchRequest = new SearchRequestDto(0, null, null, null);
        final var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/transactions/search");
                reqConfig.setBody(new Json(searchRequest));
            });
        }).convert(SearchResponseDto.class);

        assertEquals(2, result.totalPages());
        assertEquals(0, result.currentPage());
        assertEquals(txn4.toDto(), result.transactions().get(0));
        assertEquals(txn3.toDto(), result.transactions().get(1));
    }

    @Test
    public void test_searchTransactions_onlyStartDate() {
        throw new RuntimeException();
    }

    @Test
    public void test_searchTransactions_onlyEndDate() {
        throw new RuntimeException();
    }

    @Test
    public void test_searchTransactions_onlyCategories() {
        throw new RuntimeException();
    }

    @Test
    public void test_updateTransaction() {
        throw new RuntimeException();
    }

    @Test
    public void test_updateTransaction_notFound() {
        throw new RuntimeException();
    }

}
