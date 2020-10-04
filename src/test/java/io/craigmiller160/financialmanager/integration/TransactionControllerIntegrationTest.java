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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionControllerIntegrationTest {

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
