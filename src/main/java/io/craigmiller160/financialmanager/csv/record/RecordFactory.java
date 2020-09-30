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

package io.craigmiller160.financialmanager.csv.record;

import io.micrometer.core.instrument.util.StringUtils;
import io.vavr.collection.Array;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecordFactory {

    private static final DateTimeFormatter SLASH_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static ChaseRecord chaseRecord(final String rawRecord) {
        final var fields = Array.of(rawRecord.split(","))
                .map(String::trim);

        final var details = fields.get(0);
        final var postingDate = LocalDate.parse(fields.get(1), SLASH_DATE_FORMAT);
        final var description = fields.get(2);
        final var amount = Double.parseDouble(fields.get(3));
        final var type = fields.get(4);
        final var balance = !fields.get(5).isBlank() ? Double.parseDouble(fields.get(5)) : 0;
        final int checkOrSlipNumber = !fields.get(6).isBlank() ? Integer.parseInt(fields.get(6)) : 0;

        return new ChaseRecord(
                details,
                postingDate,
                description,
                amount,
                type,
                balance,
                checkOrSlipNumber
        );
    }

    public static DiscoverRecord discoverRecord(final String rawRecord) {
        final var fields = Array.of(rawRecord.split(","))
                .map(String::trim);
        final var transDate = LocalDate.parse(fields.get(0), SLASH_DATE_FORMAT);
        final var postDate = LocalDate.parse(fields.get(1), SLASH_DATE_FORMAT);
        final var description = fields.get(2);
        final var amount = Double.parseDouble(fields.get(3));
        final var category = fields.get(4);

        return new DiscoverRecord(
                transDate,
                postDate,
                description,
                amount,
                category
        );
    }

}

