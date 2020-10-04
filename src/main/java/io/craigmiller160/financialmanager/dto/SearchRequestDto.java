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

package io.craigmiller160.financialmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record SearchRequestDto(
        @JsonProperty("pageNumber") Integer pageNumber,
        @JsonProperty("startDate") LocalDate startDate,
        @JsonProperty("endDate") LocalDate endDate,
        @JsonProperty("categoryIds") List<Long> categoryIds
) {

    public int getOrDefaultPageNumber() {
        return pageNumber != null ? pageNumber : 0;
    }

    public LocalDate getOrDefaultStartDate() {
        return startDate != null ? startDate : LocalDate.MIN;
    }

    public LocalDate getOrDefaultEndDate() {
        return endDate != null ? endDate : LocalDate.MAX;
    }

    public List<Long> getOrDefaultCategoryIds() {
        return categoryIds != null && categoryIds.size() > 0 ? categoryIds : null;
    }

}
