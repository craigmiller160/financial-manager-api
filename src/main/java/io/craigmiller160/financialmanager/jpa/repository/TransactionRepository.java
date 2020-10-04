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

package io.craigmiller160.financialmanager.jpa.repository;

import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("""
    SELECT t
    FROM Transaction t
    WHERE t.postDate >= :startDate
    AND t.postDate <= :endDate
    AND (:categoryIds IS NULL OR t.categoryId IN (:categoryIds))
    ORDER BY t.postDate DESC, t.description ASC
    """)
    Page<Transaction> searchForTransactions(
            @Param("startDate") final LocalDate startDate,
            @Param("endDate") final LocalDate endDate,
            @Param("categoryIds") final List<Long> categoryIds, // TODO look for ways to not have to set this as null
            final Pageable pageRequest
    );

    @Transactional
    @Query("""
    UPDATE Transaction t
    SET t.categoryId = null
    WHERE t.categoryId = :categoryId
    """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    int removeCategoryFromTransactions(@Param("categoryId") final long categoryId);

}
