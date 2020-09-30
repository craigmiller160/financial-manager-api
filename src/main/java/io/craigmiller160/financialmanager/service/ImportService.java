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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImportService {

    private final CsvParserFactory csvParserFactory;
    private final TransactionRepository transactionRepo;

    public void doImport(final CsvSource source, final String csv) {
        // TODO need exception handling for invalid payloads
        // TODO need the authenticated user id
        final CsvParser parser = getParser(source);
        final List<Transaction> records = parser.parse(csv)
                .map(TransactionRecord::toEntity);

        transactionRepo.saveAll(records);
    }

    private CsvParser getParser(final CsvSource source) {
        return switch (source) {
            case CHASE -> csvParserFactory.getChaseCsvParser();
            case DISCOVER -> csvParserFactory.getDiscoverCsvParser();
        };
    }

}
