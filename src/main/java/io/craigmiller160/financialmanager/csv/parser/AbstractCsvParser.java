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

package io.craigmiller160.financialmanager.csv.parser;

import io.craigmiller160.financialmanager.csv.record.BaseRecord;
import io.craigmiller160.financialmanager.csv.record.TransactionRecord;
import io.craigmiller160.financialmanager.exception.CsvParsingException;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.control.Try;

public abstract class AbstractCsvParser<R extends BaseRecord> implements CsvParser {

    protected abstract Try<R> createRecord(final String rawRecord);

    protected abstract boolean acceptRecord(final R record);

    public Try<Stream<TransactionRecord>> parse(final String csv) {
        final Stream<Try<R>> records = Array.of(csv.split("\n"))
                .toStream()
                .subSequence(1)
                .map(this::createRecord);
        return Try.of(() ->
            records.map(Try::get)
                    .filter(this::acceptRecord)
                    .map(BaseRecord::toTransactionRecord)
        )
                .recoverWith(ex -> {
                    final String message = String.format("Error parsing CSV: %s: %s", ex.getClass().getName(), ex.getMessage());
                    return Try.failure(new CsvParsingException(message, ex));
                });
    }

}
