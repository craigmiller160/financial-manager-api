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

import io.craigmiller160.financialmanager.csv.record.ChaseRecord;
import io.craigmiller160.financialmanager.csv.record.RecordFactory;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class ChaseCsvParser extends AbstractCsvParser<ChaseRecord> {
    @Override
    protected Try<ChaseRecord> createRecord(final String rawRecord) {
        return Try.of(() -> RecordFactory.chaseRecord(rawRecord));
    }

    @Override
    protected boolean acceptRecord(final ChaseRecord record) {
        return !"CREDIT".equals(record.details());
    }
}
