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

package io.craigmiller160.financialmanager.jpa.entity;

import io.craigmiller160.financialmanager.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
public class Transaction implements EntityToDto<TransactionDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @With
    private Long id;
    private String description;
    @NotNull
    private Double amount;
    @NotNull
    private LocalDate postDate;
    @NotNull
    private String userId;
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Override
    public TransactionDto toDto() {
        final var categoryDto = category != null ? category.toDto() : null;
        return new TransactionDto(id, description, amount, postDate, categoryDto);
    }

}
