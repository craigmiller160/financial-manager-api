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
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public Specification<Transaction> toJpaSpecification() {
        return (final Root<Transaction> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) -> {
            final var criteria = new ArrayList<Predicate>();
            if (startDate != null) {
                criteria.add(builder.greaterThanOrEqualTo(root.get("postDate"), startDate));
            }

            if (endDate != null) {
                criteria.add(builder.lessThanOrEqualTo(root.get("postDate"), endDate));
            }

            if (categoryIds != null && categoryIds.size() > 0) {
                final var inClause = builder.in(root.get("categoryId"));
                categoryIds.forEach(inClause::value);
                criteria.add(inClause);
            }

            return builder.and(criteria.toArray(new Predicate[0]));
        };
    }

}
