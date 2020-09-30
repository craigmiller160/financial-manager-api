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

package io.craigmiller160.financialmanager.controller;

import io.craigmiller160.financialmanager.csv.CsvSource;
import io.craigmiller160.financialmanager.service.ImportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(final ImportService importService) {
        this.importService = importService;
    }

    @PostMapping(value = "/{source}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> doImport(@PathVariable final CsvSource source, @RequestBody final String csv) {
        importService.doImport(source, csv);
        return ResponseEntity.noContent().build();
    }

}
