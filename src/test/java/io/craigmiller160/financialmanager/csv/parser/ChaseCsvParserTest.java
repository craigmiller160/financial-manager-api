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

import io.craigmiller160.financialmanager.csv.record.TransactionRecord;
import io.craigmiller160.financialmanager.exception.CsvParsingException;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ChaseCsvParserTest extends AbstractCsvParserTest {

    private final ChaseCsvParser parser = new ChaseCsvParser();
    private final TransactionRecord txnRecord1 = new TransactionRecord(
            LocalDate.of(2020, 9, 28),
            "Something or Other",
            51.83
    );
    private final TransactionRecord txnRecord2 = new TransactionRecord(
            LocalDate.of(2020, 9, 27),
            "Different Thing",
            86.83
    );

    @Test
    public void test_parse() {
        final String csv = loadCsv("chase.csv")
                .getOrElseThrow(() -> new RuntimeException("Unable to load CSV file"));

        final List<TransactionRecord> records = parser.parse(csv).get().toList();
        assertEquals(2, records.size());

        assertEquals(txnRecord1, records.get(0));
        assertEquals(txnRecord2, records.get(1));
    }

    @Test
    public void test_parse_error() {
        final String csv = loadCsv("chase.csv")
                .getOrElseThrow(() -> new RuntimeException("Unable to load CSV file"));
        final String modifiedCsv = csv.replaceAll("-51\\.83", "ABCDEFG");
        final Try<Stream<TransactionRecord>> recordsTry = parser.parse(modifiedCsv);
        assertTrue(recordsTry.isFailure());
        assertTrue(recordsTry.getCause() instanceof CsvParsingException);
        assertEquals("Error parsing CSV: java.lang.NumberFormatException: For input string: \"ABCDEFG\"", recordsTry.getCause().getMessage());
    }

}
