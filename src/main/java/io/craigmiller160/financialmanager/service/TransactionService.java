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

package io.craigmiller160.financialmanager.service;

import io.craigmiller160.financialmanager.config.PaginationConfig;
import io.craigmiller160.financialmanager.dto.SearchRequestDto;
import io.craigmiller160.financialmanager.dto.SearchResponseDto;
import io.craigmiller160.financialmanager.dto.TransactionDto;
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import io.craigmiller160.financialmanager.jpa.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TransactionService {

    // TODO how to handle date timezones?

    private final TransactionRepository transactionRepo;
    private final SecurityService securityService;
    private final PaginationConfig paginationConfig;

    public TransactionDto updateTransaction(final long id, final TransactionDto payload) {
        transactionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No transactions found for id %d", id)));

        final var transaction = payload.toEntity();
        transaction.setId(id);
        transaction.setUserId(securityService.getAuthenticatedUser().getUsername());
        return transactionRepo.save(transaction).toDto();
    }

    public SearchResponseDto searchTransactions(final SearchRequestDto searchRequest) {
        var pageRequest = PageRequest.of(
                searchRequest.getOrDefaultPageNumber(),
                paginationConfig.getPageSize()
        );

        final var pageResults = transactionRepo.searchForTransactions(
                searchRequest.getOrDefaultStartDate(),
                searchRequest.getOrDefaultEndDate(),
                searchRequest.getOrDefaultCategoryIds(),
                pageRequest
        );

        final var transactions = pageResults.get()
                .map(Transaction::toDto)
                .collect(Collectors.toList());
        return new SearchResponseDto(pageResults.getTotalPages(), searchRequest.getOrDefaultPageNumber(), transactions);
    }

}
