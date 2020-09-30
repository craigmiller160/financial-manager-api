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

package io.craigmiller160.financialmanager.jpa.entity

import java.time.LocalDate

import io.craigmiller160.financialmanager.scalautil.ScalaJpaAnnotations.{GeneratedValue, Id, JoinColumn, ManyToOne}
import javax.persistence.{Entity, FetchType, GenerationType, Table}

@Entity
@Table(name = "transactions")
case class Transaction (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    id: Long,
    description: String,
    amount: Double,
    postDate: LocalDate,
    userId: String,
    categoryId: Option[Long] = None,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    category: Option[Category] = None
)
