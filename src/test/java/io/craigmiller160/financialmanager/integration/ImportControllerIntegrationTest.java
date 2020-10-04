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

package io.craigmiller160.financialmanager.integration;

import io.craigmiller160.apitestprocessor.body.Text;
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import io.craigmiller160.financialmanager.jpa.repository.TransactionRepository;
import io.craigmiller160.financialmanager.testutils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

@ExtendWith(SpringExtension.class)
public class ImportControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepo;

    @BeforeEach
    public void setup() {
        transactionRepo.deleteAll();
    }

    private void validateTransaction(final Transaction txn, final String description, final double amount, final LocalDate postDate) {
        assertThat(txn, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("description", equalTo(description)),
                hasProperty("amount", equalTo(amount)),
                hasProperty("categoryId", nullValue()),
                hasProperty("userId", equalTo(JwtUtils.USERNAME)),
                hasProperty("postDate", equalTo(postDate))
        ));
    }

    @Test
    public void test_doImport_chase() {
        final String csv = loadCsv("chase.csv");
        apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/import/CHASE");
                reqConfig.setBody(new Text(csv));
            });
            apiConfig.response(resConfig -> {
                resConfig.setStatus(204);
            });
        });

        final var allTxns = transactionRepo.findAll();
        assertEquals(2, allTxns.size());
        validateTransaction(allTxns.get(0), "Something or Other", 51.83, LocalDate.of(2020, 9, 28));
        validateTransaction(allTxns.get(1), "Different Thing", 86.83, LocalDate.of(2020, 9, 27));
    }

    @Test
    public void test_doImport_chase_badCsv() throws Exception {
        final String csv = loadCsv("chase.csv");
        final String badCsv = csv.replaceAll("-51.83", "ABC");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/import/CHASE");
                reqConfig.setBody(new Text(badCsv));
            });
            apiConfig.response(resConfig -> {
                resConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "Error parsing CSV: java.lang.NumberFormatException");
        assertEquals(0, transactionRepo.count());
    }

    @Test
    public void test_doImport_discover() {
        final String csv = loadCsv("discover.csv");
        apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/import/DISCOVER");
                reqConfig.setBody(new Text(csv));
            });
            apiConfig.response(resConfig -> {
                resConfig.setStatus(204);
            });
        });

        final var allTxns = transactionRepo.findAll();
        assertEquals(2, allTxns.size());
        validateTransaction(allTxns.get(0), "Place 1", 44.88, LocalDate.of(2020, 9, 16));
        validateTransaction(allTxns.get(1), "Place 2", 4.99, LocalDate.of(2020, 9, 15));
    }

    @Test
    public void test_doImport_discover_badCsv() {
        final String csv = loadCsv("discover.csv");
        final String badCsv = csv.replaceAll("44.88", "ABC");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/import/DISCOVER");
                reqConfig.setBody(new Text(badCsv));
            });
            apiConfig.response(resConfig -> {
                resConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "Error parsing CSV: java.lang.NumberFormatException");
        assertEquals(0, transactionRepo.count());
    }

    @Test
    public void test_doImport_invalidSource() {
        final String csv = loadCsv("chase.csv");
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(reqConfig -> {
                reqConfig.setMethod(HttpMethod.POST);
                reqConfig.setPath("/import/ABC");
                reqConfig.setBody(new Text(csv));
            });
            apiConfig.response(resConfig -> {
                resConfig.setStatus(400);
            });
        });

        validateError(result, HttpStatus.BAD_REQUEST, "Failed to convert value of type");
    }

}
