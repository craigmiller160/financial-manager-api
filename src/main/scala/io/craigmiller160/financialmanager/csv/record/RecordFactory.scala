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

package io.craigmiller160.financialmanager.csv.record

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object RecordFactory {
    private final val SLASH_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/DD/yyyy")

    def chaseRecord(rawRecord: String): ChaseRecord = {
        val fields = rawRecord.split(",")
        val details = fields(0).trim
        val postingDate = LocalDate.parse(fields(1).trim, SLASH_DATE_FORMAT)
        val description = fields(2).trim
        val amount = fields(3).trim.toDouble
        val txnType = fields(4).trim
        val balance = if(!fields(5).trim.isBlank) fields(5).trim.toDouble else 0
        val checkOrSlipNumber = if(!fields(6).trim.isBlank) fields(6).trim.toInt else 0

        ChaseRecord(
            details,
            postingDate,
            description,
            amount,
            txnType,
            balance,
            checkOrSlipNumber
        )
    }

    def discoverRecord(rawRecord: String): DiscoverRecord = {
        val fields = rawRecord.split(",")
        val transDate = LocalDate.parse(fields(0).trim, SLASH_DATE_FORMAT)
        val postDate = LocalDate.parse(fields(1).trim, SLASH_DATE_FORMAT)
        val description = fields(2).trim
        val amount = fields(3).trim.toDouble
        val category = fields(4).trim

        DiscoverRecord(
            transDate,
            postDate,
            description,
            amount,
            category
        )
    }
}
