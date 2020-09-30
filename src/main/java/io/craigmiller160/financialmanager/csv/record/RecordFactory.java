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

    // TODO refactor using vavr
    public static ChaseRecord chaseRecord(final String rawRecord) {
        final var fields = rawRecord.split(",");
        final var details = fields[0].trim();
        final var postingDate = LocalDate.parse(fields[1].trim(), SLASH_DATE_FORMAT);
        final var description = fields[2].trim();
        final var amount = Double.parseDouble(fields[3].trim());
        final var type = fields[4].trim();
        final var balance = StringUtils.isNotBlank(fields[5]) ? Double.parseDouble(fields[5].trim()) : 0;
        final int checkOrSlipNumber = StringUtils.isNotBlank(fields[6]) ? Integer.parseInt(fields[6].trim()) : 0;

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

