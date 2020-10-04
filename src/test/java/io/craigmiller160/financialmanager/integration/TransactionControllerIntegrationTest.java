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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        txn2 = transactionRepo.save(TestData.createTransaction(2L, null));
        txn3 = transactionRepo.save(TestData.createTransaction(3L, category1.getId()));
        txn4 = transactionRepo.save(TestData.createTransaction(4L, null));
    }

    @AfterEach
    public void cleanup() {
        transactionRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    public void test_searchTransactions_allParams() {
        throw new RuntimeException();
    }

    @Test
    public void test_searchTransactions_noParams() {
        throw new RuntimeException();
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
