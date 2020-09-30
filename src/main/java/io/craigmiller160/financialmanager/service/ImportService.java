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

import io.craigmiller160.financialmanager.csv.CsvSource;
import io.craigmiller160.financialmanager.csv.parser.CsvParser;
import io.craigmiller160.financialmanager.csv.parser.CsvParserFactory;
import io.craigmiller160.financialmanager.csv.record.TransactionRecord;
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import io.craigmiller160.financialmanager.jpa.repository.TransactionRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImportService {

    private final CsvParserFactory csvParserFactory;
    private final TransactionRepository transactionRepo;
    private final SecurityService securityService;

    public Try<List<Transaction>> doImport(final CsvSource source, final String csv) {
        final String userId = securityService.getAuthenticatedUser().getUsername();
        final CsvParser parser = getParser(source);
        return parser.parse(csv)
                .map(stream -> {
                    final List<Transaction> entityList = stream
                            .map(TransactionRecord::toEntity)
                            .map(txn -> {
                                txn.setUserId(userId);
                                return txn;
                            })
                            .toList();
                    return List.ofAll(transactionRepo.saveAll(entityList));
                });
    }

    private CsvParser getParser(final CsvSource source) {
        return switch (source) {
            case CHASE -> csvParserFactory.getChaseCsvParser();
            case DISCOVER -> csvParserFactory.getDiscoverCsvParser();
        };
    }

}
