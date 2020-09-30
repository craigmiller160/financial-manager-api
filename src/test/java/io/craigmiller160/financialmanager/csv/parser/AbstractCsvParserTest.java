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

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

public class AbstractCsvParserTest {

    protected Option<String> loadCsv(final String fileName) {
        return Option.of(getClass().getClassLoader().getResourceAsStream(String.format("csv/%s", fileName)))
                .flatMap(stream ->
                        Try.of(() -> IOUtils.toString(stream, StandardCharsets.UTF_8))
                                .toOption()
                );
    }

}
