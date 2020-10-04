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

package io.craigmiller160.financialmanager.testutils;

import io.craigmiller160.financialmanager.jpa.entity.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TestData {

    private TestData() {}

    public static Transaction createTransaction(final long number, final Long categoryId) {
        final Transaction txn = new Transaction();
        txn.setPostDate(LocalDate.now().plus(number, ChronoUnit.DAYS));
        txn.setDescription(String.format("Description_%d", number));
        txn.setUserId(String.format("userId_%d", number));
        txn.setAmount(20.0 + number);
        txn.setCategoryId(categoryId);
        return txn;
    }

}
