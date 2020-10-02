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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import io.craigmiller160.apitestprocessor.ApiTestProcessor;
import io.craigmiller160.apitestprocessor.body.Text;
import io.craigmiller160.apitestprocessor.config.AuthType;
import io.craigmiller160.financialmanager.jpa.entity.Transaction;
import io.craigmiller160.financialmanager.jpa.repository.TransactionRepository;
import io.craigmiller160.financialmanager.testutils.JwtUtils;
import io.craigmiller160.oauth2.config.OAuthConfig;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ImportControllerIntegrationTest {

    private static KeyPair keyPair;
    private static JWKSet jwkSet;

    @BeforeAll
    public static void keySetup() throws Exception {
        keyPair = JwtUtils.createKeyPair();
        jwkSet = JwtUtils.createJwkSet(keyPair);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionRepository transactionRepo;

    @MockBean
    private OAuthConfig oAuthConfig;
    private ApiTestProcessor apiTestProcessor;

    private String token;

    @BeforeEach
    public void setup() throws Exception {
        when(oAuthConfig.getJwkSet())
                .thenReturn(jwkSet);
        when(oAuthConfig.getClientKey())
                .thenReturn(JwtUtils.CLIENT_KEY);
        when(oAuthConfig.getClientName())
                .thenReturn(JwtUtils.CLIENT_NAME);

        final var jwt = JwtUtils.createJwt();
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.getPrivate());

        apiTestProcessor = new ApiTestProcessor(setupConfig -> {
            setupConfig.setMockMvc(mockMvc);
            setupConfig.setObjectMapper(objectMapper);
            setupConfig.auth(authConfig -> {
                authConfig.setType(AuthType.BEARER);
                authConfig.setBearerToken(token);
            });
        });

        transactionRepo.deleteAll();
    }

    private String loadCsv(final String fileName) {
        return Option.of(getClass().getClassLoader().getResourceAsStream(String.format("csv/%s", fileName)))
                .flatMap(stream ->
                        Try.of(() -> IOUtils.toString(stream, StandardCharsets.UTF_8))
                                .toOption()
                )
                .getOrElseThrow(() -> new RuntimeException("Could not load CSV file"));
    }

    private void validateTransaction(final Transaction txn, final String description, final double amount) {
        assertThat(txn, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("description", equalTo(description)),
                hasProperty("amount", equalTo(amount)),
                hasProperty("categoryId", nullValue()),
                hasProperty("userId", equalTo(JwtUtils.USERNAME))
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
        validateTransaction(allTxns.get(0), "Something or Other", 51.83);
        validateTransaction(allTxns.get(1), "Different Thing", 86.83);
    }

    @Test
    public void test_doImport_chase_badCsv() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_discover() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_discover_badCsv() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_invalidSource() {
        throw new RuntimeException();
    }

}
